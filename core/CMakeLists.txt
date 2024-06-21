cmake_minimum_required(VERSION 3.28)
project(core)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_BUILD_RPATH_USE_ORIGIN TRUE)

add_definitions(-DMAGICKCORE_HDRI_ENABLE=0 -DMAGICKCORE_QUANTUM_DEPTH=16)

set(JAVA_ROOT /usr/lib/jvm/openjdk-bin-8)
include_directories("${JAVA_ROOT}/include/")
include_directories("${JAVA_ROOT}/include/linux")

add_library(
		core SHARED

		src/core.cc
		src/jni/Core.cpp
		src/ImageLoader.cc
		src/BlurDetector.cc
		src/PipelineStep.cc
		src/Pipe.cc
		src/Util.cc
)
add_executable(exec src/main.cc)

set(SQLITE_HEADER "${CMAKE_CURRENT_LIST_DIR}/sqlite/include")
set(SQLITE_BINARY "${CMAKE_CURRENT_LIST_DIR}/sqlite/lib")

find_library(SQLITE NAMES sqlite3 PATHS "${SQLITE_BINARY}")
target_link_libraries(core PUBLIC "${SQLITE}")
target_include_directories(core PUBLIC "${SQLITE_HEADER}")

set(ONNX_RUNTIME_HEADER "${CMAKE_CURRENT_LIST_DIR}/onnxruntime/include")
set(ONNX_RUNTIME_BINARY "${CMAKE_CURRENT_LIST_DIR}/onnxruntime/lib")

find_library(ONNX_RUNTIME NAMES onnxruntime PATHS "${ONNX_RUNTIME_BINARY}")
target_link_libraries(core PUBLIC "${ONNX_RUNTIME}")
target_include_directories(core PUBLIC "${ONNX_RUNTIME_HEADER}")

find_package(ImageMagick COMPONENTS Magick++ REQUIRED)
include_directories(${ImageMagick_INCLUDE_DIRS})
target_link_libraries(core PUBLIC "${ImageMagick_LIBRARIES}")

add_dependencies(exec core)
target_link_libraries(exec PRIVATE core)
