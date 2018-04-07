FROM ubuntu:latest
RUN apt-get update && apt-get install -y openssl zlib1g-dev libboost-all-dev libssl-dev libcpprest libcpprest-dev build-essential
ADD HelloWorld /app/
EXPOSE 80
ENTRYPOINT ["/app/HelloWorld"]

## - still trying to get rid of: apt-get -y install libboost-dev libcpprest
## - build and run...
# docker build -t helloworld .
# docker run -d -p 8080:8080 helloworld
# curl -v -H "Content-Type: application/json" -X POST http://localhost:8080/auth -d '{"one" : "100", "two" : "200"}'
# curl -H "Content-Type: application/json" -X POST http://localhost:8080/auth -d '{"userid":"4500123412341234","password":"password"}'
# curl -v -H "Content-Type: application/json" -X POST http://localhost:8080/auth -d '{"userid":"1234","password":"password"}'
# curl http://localhost:8088/restdemo
