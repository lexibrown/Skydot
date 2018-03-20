#include <string>
#include <iostream>
#include <map>
#include <set>
#include <string>
#include <cpprest/json.h>
#include <cpprest/http_listener.h>
#include <cpprest/uri.h>
#include <cpprest/asyncrt_utils.h>
#include <cpprest/http_client.h>
#pragma comment(lib, "cpprest_2_10")

using namespace std;
using namespace web;
using namespace web::json;
using namespace web::http;
using namespace web::http::client;
using namespace web::http::experimental::listener;

#define TRACE(msg)            std::cout << msg;
#define TRACE_ACTION(...) std::cout , __VA_ARGS__ , L"\n";

void display_json(
   json::value const & jvalue,
   utility::string_t const & prefix)
{
   std::cout << prefix << jvalue.serialize() << endl;
}

void handle_request(
   http_request request,
   function<void(json::value const &, json::value &)> action)
{
   auto answer = json::value::object();

   request
      .extract_json()
      .then([&answer, &action](pplx::task<json::value> task) {
         try
         {
            auto const & jvalue = task.get();
            display_json(jvalue, "R: ");

            if (!jvalue.is_null())
            {
               action(jvalue, answer);
            }
         }
         catch (http_exception const & e)
         {
            std::cout << e.what() << endl;
         }
      })
      .wait();

   
   display_json(answer, "S: ");

   json::value json_return;
   http_client client("https://skydot-bank.azurewebsites.net/host/delete");

   http_response response = client.request(web::http::methods::POST, U("/"), answer)
        .then([](const web::http::http_response& response) {
            return response.extract_json(); 
        })
        .then([&json_return](const pplx::task<web::json::value>& task) {
            try {
                json_return = task.get();
            }
            catch (const web::http::http_exception& e) {                    
                std::cout << "error " << e.what() << std::endl;
            }
        })
        .wait();


   std::cout << json_return.serialize() << std::endl;
   for (auto const & e : json_return.as_object())
   {
      auto key = e.first;
      auto value = e.second;
      std::cout << key << std::endl;
      std::cout << value << std::endl;
      std::cout << key.compare("error") << std::endl;

      if (key.compare("error") == 0)
      {
         request.reply(status_codes::Unauthorized, json_return);
         return;
      }
   }
   request.reply(status_codes::OK, json_return);
}

void handle_post(http_request request)
{
   TRACE("\nhandle POST\n");

   handle_request(
      request,
      [](json::value const & jvalue, json::value & answer)
   {
      for (auto const & e : jvalue.as_object())
      {
         auto key = e.first;
         auto value = e.second;
         TRACE_ACTION(L"post", key, value);
         answer[key] = value;
      }
   });
}

int main()
{
   http_listener listener(U("http://0.0.0.0:8080/delete"));

   listener.support(methods::POST, handle_post);

   try
   {
      listener
         .open()
         .then([&listener]() {TRACE(L"\nstarting to listen\n"); })
         .wait();

      while (true);
   }
   catch (exception const & e)
   {
      std::cout << e.what() << endl;
   }

   return 0;
}