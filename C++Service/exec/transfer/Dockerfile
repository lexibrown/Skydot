FROM ubuntu:latest
RUN apt-get update && apt-get install -y openssl zlib1g-dev libboost-all-dev libssl-dev libcpprest libcpprest-dev build-essential
ADD HelloWorld /app/
EXPOSE 80
ENTRYPOINT ["/app/HelloWorld"]

## - still trying to get rid of: apt-get -y install libboost-dev libcpprest
## - build and run...
# docker build -t helloworld .
# docker run -d -p 80:80 helloworld
# curl -v -H "Content-Type: application/json" -X POST http://localhost/transfer -d '{"one" : "100", "two" : "200"}'
# curl -H "Content-Type: application/json" -X POST http://localhost/transfer -d '{"user_id":"4500123412341234","from_account":9079}'
# curl -H "Content-Type: application/json" -X POST http://localhost/transfer -d '{"user_id":"4500123412341234","from_account":9079, "to_account":9080, "amount":10, "currency":"CAD"}'
