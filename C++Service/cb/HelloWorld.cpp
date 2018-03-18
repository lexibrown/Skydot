#include <string>
#include <iostream>
#include <map>
#include <set>
#include <string>
#include <cpprest/json.h>
#include <cpprest/http_listener.h>
#include <cpprest/uri.h>
#include <cpprest/asyncrt_utils.h>
#pragma comment(lib, "cpprest_2_10")

using namespace std;
using namespace web;
using namespace web::json;
using namespace web::http;
using namespace web::http::experimental::listener;

#define TRACE(msg)            std::cout << msg;
#define TRACE_ACTION(...) std::cout , __VA_ARGS__ , L"\n";

map<utility::string_t, utility::string_t> dictionary;

void display_json(
   json::value const & jvalue,
   utility::string_t const & prefix)
{
   std::cout << prefix << jvalue.serialize() << endl;
}

void handle_get(http_request request)
{
   TRACE(L"\nhandle GET\n");

   auto answer = json::value::object();

   for (auto const & p : dictionary)
   {
      answer[p.first] = json::value::string(p.second);
   }

   display_json(json::value::null(), "R: ");
   display_json(answer, "S: ");

   request.reply(status_codes::OK, answer);
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

   request.reply(status_codes::OK, answer);
}

void handle_post(http_request request)
{
   TRACE("\nhandle POST\n");

   handle_request(
      request,
      [](json::value const & jvalue, json::value & answer)
   {
      for (auto const & e : jvalue.as_array())
      {
         if (e.is_string())
         {
            auto key = e.as_string();
            auto pos = dictionary.find(key);

            if (pos == dictionary.end())
            {
               answer[key] = json::value::string("<nil>");
            }
            else
            {
               answer[pos->first] = json::value::string(pos->second);
            }
         }
      }
   });
}

void handle_put(http_request request)
{
   TRACE("\nhandle PUT\n");

   handle_request(
      request,
      [](json::value const & jvalue, json::value & answer)
   {
      for (auto const & e : jvalue.as_object())
      {
         if (e.second.is_string())
         {
            auto key = e.first;
            auto value = e.second.as_string();

            if (dictionary.find(key) == dictionary.end())
            {
               TRACE_ACTION(L"added", key, value);
               answer[key] = json::value::string("<put>");
            }
            else
            {
               TRACE_ACTION(L"updated", key, value);
               answer[key] = json::value::string("<updated>");
            }

            dictionary[key] = value;
         }
      }
   });
}

void handle_del(http_request request)
{
   TRACE("\nhandle DEL\n");

   handle_request(
      request,
      [](json::value const & jvalue, json::value & answer)
   {
      set<utility::string_t> keys;
      for (auto const & e : jvalue.as_array())
      {
         if (e.is_string())
         {
            auto key = e.as_string();

            auto pos = dictionary.find(key);
            if (pos == dictionary.end())
            {
               answer[key] = json::value::string("<failed>");
            }
            else
            {
               TRACE_ACTION(L"deleted", pos->first, pos->second);
               answer[key] = json::value::string("<deleted>");
               keys.insert(key);
            }
         }
      }

      for (auto const & key : keys)
         dictionary.erase(key);
   });
}

int main()
{
   http_listener listener(U("http://localhost:8080/restdemo"));

   listener.support(methods::GET,  handle_get);
   listener.support(methods::POST, handle_post);
   listener.support(methods::PUT,  handle_put);
   listener.support(methods::DEL,  handle_del);

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