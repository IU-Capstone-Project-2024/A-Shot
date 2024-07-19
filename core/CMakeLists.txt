cmake_minimum_required(VERSION 3.22)

set(PRJ my_puper_lib)

project(${PRJ})

set(CMAKE_CXX_STANDARD 17)

find_package(ZLIB REQUIRED)
find_package(OpenMP REQUIRED)

include(ExternalProject)

# TODO:
if (NOT DEFINED CMAKE_BUILD_PARALLEL_LEVEL)
	message(WARNING "CMAKE_BUILD_PARALLEL_LEVEL is not defined, using default: 1")
	set(CMAKE_BUILD_PARALLEL_LEVEL 1)
endif ()

# region onnxruntime
set(ONNXRUNTIME_INSTALL "${CMAKE_CURRENT_BINARY_DIR}/onnxruntime")
set(ONNXRUNTIME_INCLUDE "${ONNXRUNTIME_INSTALL}/include")
set(ONNXRUNTIME_LIB "${ONNXRUNTIME_INSTALL}/lib")

set(onnxruntime_patch "${CMAKE_CURRENT_BINARY_DIR}/onnxruntime.patch.sh")
set(onnxruntime_build "${CMAKE_CURRENT_BINARY_DIR}/onnxruntime.build.sh")
set(onnxruntime_install "${CMAKE_CURRENT_BINARY_DIR}/onnxruntime.install.sh")

ExternalProject_Add(
		onnxruntime
		GIT_REPOSITORY https://github.com/microsoft/onnxruntime.git
		GIT_TAG "v1.18.1"
		GIT_SUBMODULES_RECURSE True
		GIT_PROGRESS True
		PATCH_COMMAND "${onnxruntime_patch}"
		CONFIGURE_COMMAND ""
		BUILD_COMMAND "${onnxruntime_build}"
		INSTALL_COMMAND "${onnxruntime_install}"
		INSTALL_BYPRODUCTS "${ONNXRUNTIME_LIB}/libonnxruntime.a"
)

ExternalProject_Get_property(onnxruntime SOURCE_DIR)
configure_file(onnxruntime.patch.sh.in "${onnxruntime_patch}" FILE_PERMISSIONS OWNER_READ OWNER_WRITE OWNER_EXECUTE GROUP_READ GROUP_EXECUTE WORLD_READ WORLD_EXECUTE)
configure_file(onnxruntime.build.sh.in "${onnxruntime_build}" FILE_PERMISSIONS OWNER_READ OWNER_WRITE OWNER_EXECUTE GROUP_READ GROUP_EXECUTE WORLD_READ WORLD_EXECUTE)
configure_file(onnxruntime.install.sh.in "${onnxruntime_install}" FILE_PERMISSIONS OWNER_READ OWNER_WRITE OWNER_EXECUTE GROUP_READ GROUP_EXECUTE WORLD_READ WORLD_EXECUTE)
#endregion onnxruntime
# region imagemagick

# region jpeg
set(JPEG_INSTALL "${CMAKE_CURRENT_BINARY_DIR}/jpeg")
set(JPEG_INCLUDE "${JPEG_INSTALL}/include")
set(JPEG_LIB "${JPEG_INSTALL}/lib")

ExternalProject_Add(
		jpeg
		GIT_REPOSITORY https://github.com/libjpeg-turbo/libjpeg-turbo.git
		GIT_TAG "3.0.3"
		GIT_PROGRESS True
		CMAKE_ARGS -DCMAKE_C_FLAGS="-fPIC" -DCMAKE_INSTALL_PREFIX=${JPEG_INSTALL} -DCMAKE_INSTALL_INCLUDEDIR=${JPEG_INCLUDE} -DCMAKE_INSTALL_LIBDIR=${JPEG_LIB}
		INSTALL_BYPRODUCTS "${JPEG_LIB}/libjpeg.a"
)
# endregion jpeg
# region png
set(PNG_INSTALL "${CMAKE_CURRENT_BINARY_DIR}/png")
set(PNG_INCLUDE "${PNG_INSTALL}/include")
set(PNG_LIB "${PNG_INSTALL}/lib")

ExternalProject_Add(
		png
		GIT_REPOSITORY https://github.com/pnggroup/libpng.git
		GIT_TAG "v1.6.43"
		GIT_PROGRESS True
		CMAKE_ARGS -DCMAKE_C_FLAGS="-fPIC" -DCMAKE_INSTALL_PREFIX=${PNG_INSTALL} -DCMAKE_INSTALL_INCLUDEDIR=${PNG_INCLUDE} -DCMAKE_INSTALL_LIBDIR=${PNG_LIB}
		INSTALL_BYPRODUCTS "${PNG_LIB}/libpng.a"
)
# endregion png
# region lcms
set(LCMS_INSTALL "${CMAKE_CURRENT_BINARY_DIR}/lcms")
set(LCMS_INCLUDE "${LCMS_INSTALL}/include")
set(LCMS_LIB "${LCMS_INSTALL}/lib")

set(LCMS_CXXFLAGS "-fPIC")
set(LCMS_CFLAGS "-fPIC")

set(lcms_configure "${CMAKE_CURRENT_BINARY_DIR}/lcms.configure.sh")
set(lcms_build "${CMAKE_CURRENT_BINARY_DIR}/lcms.build.sh")
set(lcms_install "${CMAKE_CURRENT_BINARY_DIR}/lcms.install.sh")

ExternalProject_Add(
		lcms
		GIT_REPOSITORY https://github.com/mm2/Little-CMS.git
		GIT_TAG "lcms2.16"
		GIT_PROGRESS True
		CONFIGURE_COMMAND "${lcms_configure}"
		BUILD_COMMAND "${lcms_build}"
		INSTALL_COMMAND "${lcms_install}"
		DEPENDS jpeg
		INSTALL_BYPRODUCTS "${LCMS_LIB}/liblcms2.a" "${LCMS_LIB}/liblcms2_fast_float.a" "${LCMS_LIB}/liblcms2_threaded.a"
)

ExternalProject_Get_property(lcms SOURCE_DIR)
configure_file(lcms.configure.sh.in "${lcms_configure}" FILE_PERMISSIONS OWNER_READ OWNER_WRITE OWNER_EXECUTE GROUP_READ GROUP_EXECUTE WORLD_READ WORLD_EXECUTE)
configure_file(lcms.build.sh.in "${lcms_build}" FILE_PERMISSIONS OWNER_READ OWNER_WRITE OWNER_EXECUTE GROUP_READ GROUP_EXECUTE WORLD_READ WORLD_EXECUTE)
configure_file(lcms.install.sh.in "${lcms_install}" FILE_PERMISSIONS OWNER_READ OWNER_WRITE OWNER_EXECUTE GROUP_READ GROUP_EXECUTE WORLD_READ WORLD_EXECUTE)
# endregion lcms
# region raw
set(RAW_INSTALL "${CMAKE_CURRENT_BINARY_DIR}/raw")
set(RAW_INCLUDE "${RAW_INSTALL}/include")
set(RAW_LIB "${RAW_INSTALL}/lib")

set(RAW_CXXFLAGS "-fPIC")
set(RAW_CFLAGS "-fPIC")

set(raw_configure "${CMAKE_CURRENT_BINARY_DIR}/raw.configure.sh")
set(raw_build "${CMAKE_CURRENT_BINARY_DIR}/raw.build.sh")
set(raw_install "${CMAKE_CURRENT_BINARY_DIR}/raw.install.sh")

ExternalProject_Add(
		raw
		GIT_REPOSITORY https://github.com/LibRaw/LibRaw.git
		GIT_TAG "0.21.2"
		GIT_PROGRESS True
		CONFIGURE_COMMAND "${raw_configure}"
		BUILD_COMMAND "${raw_build}"
		INSTALL_COMMAND "${raw_install}"
		DEPENDS lcms
		INSTALL_BYPRODUCTS "${RAW_LIB}/libraw_r.a"
)

ExternalProject_Get_property(raw SOURCE_DIR)
configure_file(raw.configure.sh.in "${raw_configure}" FILE_PERMISSIONS OWNER_READ OWNER_WRITE OWNER_EXECUTE GROUP_READ GROUP_EXECUTE WORLD_READ WORLD_EXECUTE)
configure_file(raw.build.sh.in "${raw_build}" FILE_PERMISSIONS OWNER_READ OWNER_WRITE OWNER_EXECUTE GROUP_READ GROUP_EXECUTE WORLD_READ WORLD_EXECUTE)
configure_file(raw.install.sh.in "${raw_install}" FILE_PERMISSIONS OWNER_READ OWNER_WRITE OWNER_EXECUTE GROUP_READ GROUP_EXECUTE WORLD_READ WORLD_EXECUTE)

# endregion raw
set(IMAGEMAGICK_INSTALL "${CMAKE_CURRENT_BINARY_DIR}/imagemagick")
set(IMAGEMAGICK_INCLUDE "${IMAGEMAGICK_INSTALL}/include")
set(IMAGEMAGICK_LIB "${IMAGEMAGICK_INSTALL}/lib")

set(IMAGEMAGICK_CXXFLAGS "-fPIC")
set(IMAGEMAGICK_CFLAGS "-fPIC")

if (NOT DEFINED IMAGEMAGICK_QUANTUM_DEPTH)
	message(WARNING "IMAGEMAGICK_QUANTUM_DEPTH is not defined, using default: 8")
	set(IMAGEMAGICK_QUANTUM_DEPTH 8)
endif ()

if (NOT DEFINED IMAGEMAGICK_HDRI_ENABLED)
	message(WARNING "IMAGEMAGICK_HDRI_ENABLED is not defined, using default: 0")
	set(IMAGEMAGICK_HDRI_ENABLED 0)
endif ()

add_definitions(-DMAGICKCORE_QUANTUM_DEPTH=${IMAGEMAGICK_QUANTUM_DEPTH})
add_definitions(-DMAGICKCORE_HDRI_ENABLE=${IMAGEMAGICK_HDRI_ENABLED})

if (${IMAGEMAGICK_HDRI_ENABLED})
	set(IMAGEMAGICK_HDRI_OPTION "--enable-hdri")
else ()
	set(IMAGEMAGICK_HDRI_OPTION "--disable-hdri")
endif ()

if (${IMAGEMAGICK_HDRI_ENABLED})
	set(IMAGEMAGICK_HDRI_POSTFIX "HDRI")
else ()
	set(IMAGEMAGICK_HDRI_POSTFIX "")
endif ()

set(imagemagick_patch "${CMAKE_CURRENT_BINARY_DIR}/imagemagick.patch.sh")
set(imagemagick_configure "${CMAKE_CURRENT_BINARY_DIR}/imagemagick.configure.sh")
set(imagemagick_build "${CMAKE_CURRENT_BINARY_DIR}/imagemagick.build.sh")
set(imagemagick_install "${CMAKE_CURRENT_BINARY_DIR}/imagemagick.install.sh")

ExternalProject_Add(
		imagemagick
		GIT_REPOSITORY https://github.com/ImageMagick/ImageMagick.git
		GIT_TAG "7.1.1-35"
		GIT_PROGRESS True
		PATCH_COMMAND "${imagemagick_patch}"
		CONFIGURE_COMMAND "${imagemagick_configure}"
		BUILD_COMMAND "${imagemagick_build}"
		INSTALL_COMMAND "${imagemagick_install}"
		DEPENDS jpeg png raw lcms
		INSTALL_BYPRODUCTS "${IMAGEMAGICK_LIB}/libMagick++.a" "${IMAGEMAGICK_LIB}/libMagickCore.a" "${IMAGEMAGICK_LIB}/libMagickWand.a"
)

ExternalProject_Get_property(imagemagick SOURCE_DIR)
configure_file(imagemagick.patch.sh.in "${imagemagick_patch}" FILE_PERMISSIONS OWNER_READ OWNER_WRITE OWNER_EXECUTE GROUP_READ GROUP_EXECUTE WORLD_READ WORLD_EXECUTE)
configure_file(imagemagick.configure.sh.in "${imagemagick_configure}" FILE_PERMISSIONS OWNER_READ OWNER_WRITE OWNER_EXECUTE GROUP_READ GROUP_EXECUTE WORLD_READ WORLD_EXECUTE)
configure_file(imagemagick.build.sh.in "${imagemagick_build}" FILE_PERMISSIONS OWNER_READ OWNER_WRITE OWNER_EXECUTE GROUP_READ GROUP_EXECUTE WORLD_READ WORLD_EXECUTE)
configure_file(imagemagick.install.sh.in "${imagemagick_install}" FILE_PERMISSIONS OWNER_READ OWNER_WRITE OWNER_EXECUTE GROUP_READ GROUP_EXECUTE WORLD_READ WORLD_EXECUTE)
# endregion imagemagick
# region jni
if (NOT DEFINED JAVA_ROOT)
	message(FATAL_ERROR "JAVA_ROOT is not defined")
endif ()

list(
		APPEND JNI_INCLUDE

		"${JAVA_ROOT}/include/"
		"${JAVA_ROOT}/include/linux"
)
# endregion jni

add_library(
		core SHARED

		src/jni/jni_core.cc
		src/jni/LoadingPipeline.cc

		src/core.cc

		src/ImageLoader.cc
		src/BlurDetector.cc
		src/ImageEncoder.cc

		src/PipelineStep.cc
		src/Pipe.cc

		src/Util.cc
)

add_dependencies(
		core

		onnxruntime
		imagemagick
)

target_link_libraries(
		core PRIVATE

		"${ONNXRUNTIME_LIB}/libonnxruntime.a"

		"${IMAGEMAGICK_LIB}/libMagick++.a"
		"${IMAGEMAGICK_LIB}/libMagickWand.a"
		"${IMAGEMAGICK_LIB}/libMagickCore.a"

		"${JPEG_LIB}/libjpeg.a"
		"${RAW_LIB}/libraw_r.a"
		"${PNG_LIB}/libpng.a"

		"${LCMS_LIB}/liblcms2.a"
		"${LCMS_LIB}/liblcms2_fast_float.a"
		"${LCMS_LIB}/liblcms2_threaded.a"

		${ZLIB_LIBRARIES}
		${OpenMP_C_LIBRARIES}
		#		OpenMP::OpenMP_CXX
)

target_include_directories(
		core PRIVATE

		${ONNXRUNTIME_INCLUDE}
		${IMAGEMAGICK_INCLUDE}
		${JPEG_INCLUDE}
		${RAW_INCLUDE}
		${PNG_INCLUDE}

		${JNI_INCLUDE}

		${ZLIB_INCLUDE_DIRS}
		${OpenMP_C_INCLUDE_DIRS}
		${OpenMP_CXX_INCLUDE_DIRS}
)
