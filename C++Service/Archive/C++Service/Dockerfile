FROM ubuntu:latest
RUN apt-get update && apt-get install -y g++ curl git make libboost-all-dev libssl-dev cmake libcpprest libcpprest-dev build-essential zlib1g-dev
RUN git clone https://github.com/Microsoft/cpprestsdk.git casablanca && \
 cd casablanca/Release && \
 mkdir build && \
 cd build && \
 cmake .. -DCMAKE_BUILD_TYPE=Release -DBUILD_SHARED_LIBS=0 -DCMAKE_CXX_FLAGS=-std=c++11 && \
 make
CMD ["/bin/bash"]

## - run these only once
## - this builds the Docker image
# docker build -t cbuilder .
#
## - this runs the Docker image and connects you
## - this also links the folder cb in the current folder to the docker image folders
# mkdir cb
# cp HelloWorld.cpp cb/.
# docker run -it -v $PWD/cb:/cb --name cb cbuilder /bin/bash
# cd /cb
# cmake -DCMAKE_BUILD_TYPE=Release -DBUILD_SHARED_LIBS=0 -DCMAKE_CXX_FLAGS=-std=c++11
# make
# ./HelloWorld &
#
# curl -H "Content-Type: application/json" -X PUT http://localhost:8088/restdemo -d '{"one" : "100", "two" : "200"}'
# curl http://localhost:8088/restdemo
#
# exit
## - run these whenever you want to re-start, connect and build C++ in Linux
# docker start cb
# docker attach cb
#
# ...do more
#
# exit
