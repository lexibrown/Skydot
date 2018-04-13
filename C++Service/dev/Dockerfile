FROM ubuntu:latest
RUN apt-get update && apt-get install -y g++ curl openssl git make zlib1g-dev libboost-all-dev libssl-dev cmake libcpprest libcpprest-dev build-essential

#ENV LD_LIBRARY_PATH=/lib
#ENV CPLUS_INCLUDE_PATH=/usr/include

CMD ["/bin/bash"]

## - run these only once
## - this builds the Docker image
# docker build -t cbuilder .
# mkdir cb
# cp HelloWorld.cpp cb/.
#
## - this runs the Docker image and connects you
## - this also links the folder cb in the current folder to the docker image folders
# docker run -it -v $PWD/cb:/cb --name cb cbuilder /bin/bash
# cd cb
# cmake -DCMAKE_CXX_FLAGS=-std=c++11
# make
# ./HelloWorld &
#
# curl -v -H "Content-Type: application/json" -X POST http://localhost:8080/auth -d '{"one" : "100", "two" : "200"}'
# curl -H "Content-Type: application/json" -X POST http://localhost:8080/auth -d '{"userid":"4500123412341234","password":"password"}'
# curl -v -H "Content-Type: application/json" -X POST http://localhost:8080/auth -d '{"userid":"1234","password":"password"}'
# curl http://localhost:8080/restdemo
#
# exit
## - run these whenever you want to re-start, connect and build C++ in Linux
# docker start cb
# docker attach cb
#
# ...do more
#
# exit
