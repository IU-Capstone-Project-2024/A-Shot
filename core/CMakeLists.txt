cmake_minimum_required(VERSION 3.28)
project(core)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_BUILD_RPATH_USE_ORIGIN TRUE)

set(JAVA_ROOT /usr/lib/jvm/openjdk-bin-8)
include_directories("${JAVA_ROOT}/include/")
include_directories("${JAVA_ROOT}/include/linux")

add_library(core SHARED src/core.cpp src/jni/Core.cpp)
add_executable(exec src/main.cpp)

set(ONNX_RUNTIME_HEADER "${CMAKE_CURRENT_LIST_DIR}/onnxruntime/include")
set(ONNX_RUNTIME_BINARY "${CMAKE_CURRENT_LIST_DIR}/onnxruntime/lib")

find_library(ONNX_RUNTIME NAMES onnxruntime PATHS "${ONNX_RUNTIME_BINARY}")
target_link_libraries(core PRIVATE "${ONNX_RUNTIME}")
target_include_directories(core PRIVATE "${ONNX_RUNTIME_HEADER}")

find_package(ImageMagick COMPONENTS Magick++ REQUIRED)
include_directories(${ImageMagick_INCLUDE_DIRS})
target_link_libraries(core PRIVATE "${ImageMagick_LIBRARIES}")

add_dependencies(exec core)
target_link_libraries(exec PRIVATE core)
