cmake_minimum_required(VERSION 3.22)
project(core)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_BUILD_RPATH_USE_ORIGIN TRUE)

add_library(
		core SHARED

		src/jni/jni_core.cc
		src/jni/LoadingPipeline.cc

		src/core.cc

		src/ImageLoader.cc
		src/BlurDetector.cc

		src/PipelineStep.cc
		src/Pipe.cc

		src/Util.cc
)
add_executable(exec src/main.cc)

#set(SQLITE_HEADER "${CMAKE_CURRENT_LIST_DIR}/sqlite/include")
#set(SQLITE_BINARY "${CMAKE_CURRENT_LIST_DIR}/sqlite/lib")

#find_library(SQLITE NAMES sqlite3 PATHS "${SQLITE_BINARY}")
#target_link_libraries(core PUBLIC "${SQLITE}")
#target_include_directories(core PUBLIC "${SQLITE_HEADER}")

#region onnxruntime
set(ONNX_RUNTIME_HEADER "${CMAKE_CURRENT_LIST_DIR}/onnxruntime/include")
set(ONNX_RUNTIME_BINARY "${CMAKE_CURRENT_LIST_DIR}/onnxruntime/lib")

find_library(ONNX_RUNTIME NAMES onnxruntime PATHS "${ONNX_RUNTIME_BINARY}")
target_link_libraries(core PUBLIC "${ONNX_RUNTIME}")
target_include_directories(core PUBLIC "${ONNX_RUNTIME_HEADER}")

if (NOT DEFINED BLUR_MODEL_PATH)
	message(FATAL_ERROR "BLUR_MODEL_PATH is not defined")
endif ()
add_definitions(-DBLUR_MODEL_PATH="${BLUR_MODEL_PATH}")
#endregion onnxruntime

#region ImageMagick
add_definitions(-DMAGICKCORE_HDRI_ENABLE=0 -DMAGICKCORE_QUANTUM_DEPTH=8)

set(IMAGE_MAGICK_HEADER "${CMAKE_CURRENT_LIST_DIR}/imagemagick/include/")
set(IMAGE_MAGICK_BINARY "${CMAKE_CURRENT_LIST_DIR}/imagemagick/lib/")

find_library(IMAGE_MAGICK PUBLIC Magick++ PATHS "${IMAGE_MAGICK_BINARY}")
target_link_libraries(core PUBLIC "${IMAGE_MAGICK}")
target_include_directories(core PUBLIC ${IMAGE_MAGICK_HEADER})
#endregion ImageMagick

#region Java
if (NOT DEFINED JAVA_ROOT)
	message(FATAL_ERROR "JAVA_ROOT is not defined")
endif ()

target_include_directories(core PRIVATE "${JAVA_ROOT}/include/")
target_include_directories(core PRIVATE "${JAVA_ROOT}/include/linux")
#endregion Java

set_target_properties(core PROPERTIES LINK_FLAGS "-Wl,-rpath,.")

add_dependencies(exec core)
target_link_libraries(exec PRIVATE core)



link_directories(/path/to/imagemagick/lib)
set(CMAKE_INSTALL_RPATH_USE_LINK_PATH TRUE)
set(CMAKE_INSTALL_RPATH "/path/to/imagemagick/lib")

find_package(TIFF REQUIRED)
find_package(JPEG REQUIRED)
target_link_libraries(core PUBLIC ${TIFF_LIBRARIES} ${JPEG_LIBRARIES} ${LibRaw_LIBRARIES} ${LCMS2_LIBRARIES})
target_include_directories(core PUBLIC ${TIFF_INCLUDE_DIR} ${JPEG_INCLUDE_DIR} ${LibRaw_INCLUDE_DIR} ${LCMS2_INCLUDE_DIR})

