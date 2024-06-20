cmake_minimum_required(VERSION 3.28)
project(core)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_BUILD_RPATH_USE_ORIGIN TRUE)

add_library(core SHARED src/core.cpp)
add_executable(exec src/main.cpp)

set(ONNX_RUNTIME_HEADER "${CMAKE_CURRENT_LIST_DIR}/onnxruntime/include/")
set(ONNX_RUNTIME_BINARY "${CMAKE_CURRENT_LIST_DIR}/onnxruntime/lib/")

find_library(ONNX_RUNTIME NAMES onnxruntime PATHS "${ONNX_RUNTIME_BINARY}")
target_link_libraries(core PRIVATE "${ONNX_RUNTIME}")
target_include_directories(core PRIVATE "${ONNX_RUNTIME_HEADER}")

add_dependencies(exec core)
target_link_libraries(exec PRIVATE core)
