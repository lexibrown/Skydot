cmake_minimum_required(VERSION 3.1.0 FATAL_ERROR)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11")
project(HelloWorld CXX)

add_executable(HelloWorld HelloWorld.cpp)
target_link_libraries(HelloWorld boost_system crypto ssl cpprest pthread)
