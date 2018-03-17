FROM gcc:4.9
FROM ubuntu:16.04
ADD . /HelloWorld
WORKDIR /HelloWorld
COPY . /HelloWorld
RUN apt-get update && apt-get install -y libcpprest build-essential g++
RUN g++ -o HelloWorld HelloWorld.cpp
CMD ["./HelloWorld"]