FROM gcc:4.9
ADD . /HelloWorld
WORKDIR /HelloWorld
COPY . /HelloWorld
RUN g++ -o HelloWorld HelloWorld.cpp
CMD ["./HelloWorld"]