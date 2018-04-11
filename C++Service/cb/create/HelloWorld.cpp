#include <stdio.h>
#include <time.h>
#include <sys/time.h>
#include <math.h>
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
#define __SHORT_FILE__ (strrchr(__FILE__, '/') ? strrchr(__FILE__, '/') + 1 : __FILE__)
#define __LOG__(format, loglevel, ...) printf("%s %-5s [%s] [%s:%d] " format "\n", getFormattedTime(), loglevel, __func__, __SHORT_FILE__, __LINE__, ## __VA_ARGS__)

#define LOGDEBUG(format, ...) __LOG__(format, "DEBUG", ## __VA_ARGS__)
#define LOGWARN(format, ...) __LOG__(format, "WARN", ## __VA_ARGS__)
#define LOGERROR(format, ...) __LOG__(format, "ERROR", ## __VA_ARGS__)
#define LOGINFO(format, ...) __LOG__(format, "INFO", ## __VA_ARGS__)

char* getFormattedTime(void) 
{
  // Must be static, otherwise won't work
  static char buffer[64];
  int millisec;
  struct tm* tm_info;
  struct timeval tv;

  gettimeofday(&tv, NULL);

  millisec = lrint(tv.tv_usec / 1000.0); // Round to nearest millisec
  if (millisec >= 1000) { // Allow for rounding up to nearest second
    millisec -= 1000;
    tv.tv_sec++;
  }

  tm_info = localtime(&tv.tv_sec);
  
  strftime(buffer, sizeof(buffer), "%Y-%m-%d %H:%M:%S.", tm_info);

  char millisec_str[32];
  sprintf(millisec_str, "%03d", millisec);
  strcat(buffer, millisec_str);

  return buffer;
}

// void display_json(
//    json::value const & jvalue,
//    utility::string_t const & prefix)
// {
//    std::cout << prefix << jvalue.serialize() << endl;
// }

void display_json(
   json::value const & jvalue,
   utility::string_t const & prefix)
{
   std::string str_json = jvalue.serialize();
   const char* json = str_json.c_str();
   LOGINFO("%s %s", prefix.c_str(), json);
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

   display_json(answer, "[REQUEST]");

   json::value json_return;
   http_client client("http://host-gateway/host/create");
   int crash = 0;

   http_response response = client.request(web::http::methods::POST, U("/"), answer)
        .then([](const web::http::http_response& response) {
            return response.extract_json(); 
        })
        .then([&json_return, &request, &crash](const pplx::task<web::json::value>& task) {
            try {
                json_return = task.get();
            }
            catch (const web::http::http_exception& e) {                    
                std::cout << "error " << e.what() << std::endl;
                json::value error_return;
                error_return["error"] = json::value::string("AUTH-5000");
                error_return["message"] = json::value::string("Something went wrong. Please try again.");
                display_json(error_return, "[RESPONSE]");
                request.reply(status_codes::InternalError, error_return);
                crash = 1;
            }
        })
        .wait();

   if (crash == 1) {
    return;
   }

   display_json(json_return, "[RESPONSE]");

   for (auto const & e : json_return.as_object())
   {
      auto key = e.first;
      auto value = e.second;

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
   LOGINFO("handle POST");

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
   http_listener listener(U("http://0.0.0.0:80/create"));

   listener.support(methods::POST, handle_post);
   listener
         .open()
         .then([&listener]() {TRACE(L"\nstarting to listen\n"); })
         .wait();

   try
   {
      sleep(6000);
      while (true);
   }
   catch (exception const & e)
   {
      std::cout << e.what() << endl;
   }

   return 0;
}