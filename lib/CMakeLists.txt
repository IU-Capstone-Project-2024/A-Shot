cmake_minimum_required(VERSION 3.28)
project(lib)

set(CMAKE_CXX_STANDARD 17)

add_library(lib SHARED library.cpp)
