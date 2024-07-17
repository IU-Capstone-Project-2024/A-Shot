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
		src/ImageEncoder.cc

		src/PipelineStep.cc
		src/Pipe.cc

		src/Util.cc
)
add_executable(exec src/main.cc)

#region onnxruntime
set(ONNX_RUNTIME_DIR "${CMAKE_CURRENT_LIST_DIR}/onnxruntime")
set(ONNX_RUNTIME_INCLUDE_DIR "${ONNX_RUNTIME_DIR}/include")
set(ONNX_RUNTIME_LIB_DIR "${ONNX_RUNTIME_DIR}/lib")
set(ONNX_RUNTIME_DEPS_DIR "${ONNX_RUNTIME_DIR}/deps")

add_library(libonnxruntime INTERFACE)

# region onnx
add_library(onnx STATIC IMPORTED)
add_library(onnx_proto STATIC IMPORTED)
add_library(onnx_test_data_proto STATIC IMPORTED)
add_library(onnx_test_runner_common STATIC IMPORTED)
add_library(onnxruntime_common STATIC IMPORTED)
add_library(onnxruntime_flatbuffers STATIC IMPORTED)
add_library(onnxruntime_framework STATIC IMPORTED)
add_library(onnxruntime_graph STATIC IMPORTED)
add_library(onnxruntime_mlas STATIC IMPORTED)
add_library(onnxruntime_optimizer STATIC IMPORTED)
add_library(onnxruntime_providers STATIC IMPORTED)
add_library(onnxruntime_session STATIC IMPORTED)
add_library(onnxruntime_test_utils STATIC IMPORTED)
add_library(onnxruntime_util STATIC IMPORTED)

set_property(TARGET onnx PROPERTY IMPORTED_LOCATION "${ONNX_RUNTIME_LIB_DIR}/libonnx.a")
set_property(TARGET onnx_proto PROPERTY IMPORTED_LOCATION "${ONNX_RUNTIME_LIB_DIR}/libonnx_proto.a")
set_property(TARGET onnx_test_data_proto PROPERTY IMPORTED_LOCATION "${ONNX_RUNTIME_LIB_DIR}/libonnx_test_data_proto.a")
set_property(TARGET onnx_test_runner_common PROPERTY IMPORTED_LOCATION "${ONNX_RUNTIME_LIB_DIR}/libonnx_test_runner_common.a")
set_property(TARGET onnxruntime_common PROPERTY IMPORTED_LOCATION "${ONNX_RUNTIME_LIB_DIR}/libonnxruntime_common.a")
set_property(TARGET onnxruntime_flatbuffers PROPERTY IMPORTED_LOCATION "${ONNX_RUNTIME_LIB_DIR}/libonnxruntime_flatbuffers.a")
set_property(TARGET onnxruntime_framework PROPERTY IMPORTED_LOCATION "${ONNX_RUNTIME_LIB_DIR}/libonnxruntime_framework.a")
set_property(TARGET onnxruntime_graph PROPERTY IMPORTED_LOCATION "${ONNX_RUNTIME_LIB_DIR}/libonnxruntime_graph.a")
set_property(TARGET onnxruntime_mlas PROPERTY IMPORTED_LOCATION "${ONNX_RUNTIME_LIB_DIR}/libonnxruntime_mlas.a")
set_property(TARGET onnxruntime_optimizer PROPERTY IMPORTED_LOCATION "${ONNX_RUNTIME_LIB_DIR}/libonnxruntime_optimizer.a")
set_property(TARGET onnxruntime_providers PROPERTY IMPORTED_LOCATION "${ONNX_RUNTIME_LIB_DIR}/libonnxruntime_providers.a")
set_property(TARGET onnxruntime_session PROPERTY IMPORTED_LOCATION "${ONNX_RUNTIME_LIB_DIR}/libonnxruntime_session.a")
set_property(TARGET onnxruntime_test_utils PROPERTY IMPORTED_LOCATION "${ONNX_RUNTIME_LIB_DIR}/libonnxruntime_test_utils.a")
set_property(TARGET onnxruntime_util PROPERTY IMPORTED_LOCATION "${ONNX_RUNTIME_LIB_DIR}/libonnxruntime_util.a")
# endregion onnx
# region protobuf
set(PROTOBUF_DIR "${ONNX_RUNTIME_DEPS_DIR}/protobuf")
set(PROTOBUF_LIB_DIR "${PROTOBUF_DIR}/lib")

add_library(libprotobuf INTERFACE)

add_library(protobuf STATIC IMPORTED)
add_library(protobuf_lite STATIC IMPORTED)
add_library(protoc STATIC IMPORTED)

set_property(TARGET protobuf PROPERTY IMPORTED_LOCATION "${PROTOBUF_LIB_DIR}/libprotobuf.a")
set_property(TARGET protobuf_lite PROPERTY IMPORTED_LOCATION "${PROTOBUF_LIB_DIR}/libprotobuf-lite.a")
set_property(TARGET protoc PROPERTY IMPORTED_LOCATION "${PROTOBUF_LIB_DIR}/libprotoc.a")

target_link_libraries(
		libprotobuf INTERFACE

		$<LINK_LIBRARY:WHOLE_ARCHIVE,protobuf>
#		protobuf_lite
		$<LINK_LIBRARY:WHOLE_ARCHIVE,protoc>
)
# endregion protobuf
# region nsync
set(NSYNC_DIR "${ONNX_RUNTIME_DEPS_DIR}/nsync")
set(NSYNC_LIB_DIR "${NSYNC_DIR}/lib")

add_library(libnsync INTERFACE)

add_library(nsync_cpp STATIC IMPORTED)

set_property(TARGET nsync_cpp PROPERTY IMPORTED_LOCATION "${NSYNC_LIB_DIR}/libnsync_cpp.a")

target_link_libraries(
		libnsync INTERFACE

		$<LINK_LIBRARY:WHOLE_ARCHIVE,nsync_cpp>
)
# endregion nsync
#region abseil
set(ABSL_DIR "${ONNX_RUNTIME_DEPS_DIR}/abseil")
set(ABSL_LIB_DIR "${ABSL_DIR}/lib")

add_library(libabsl INTERFACE)

add_library(absl_bad_optional_access STATIC IMPORTED)
add_library(absl_bad_variant_access STATIC IMPORTED)
add_library(absl_base STATIC IMPORTED)
add_library(absl_city STATIC IMPORTED)
add_library(absl_civil_time STATIC IMPORTED)
add_library(absl_cord STATIC IMPORTED)
add_library(absl_cord_internal STATIC IMPORTED)
add_library(absl_cordz_functions STATIC IMPORTED)
add_library(absl_cordz_handle STATIC IMPORTED)
add_library(absl_cordz_info STATIC IMPORTED)
add_library(absl_crc_cord_state STATIC IMPORTED)
add_library(absl_crc_cpu_detect STATIC IMPORTED)
add_library(absl_crc_internal STATIC IMPORTED)
add_library(absl_crc32c STATIC IMPORTED)
add_library(absl_debugging_internal STATIC IMPORTED)
add_library(absl_decode_rust_punycode STATIC IMPORTED)
add_library(absl_demangle_internal STATIC IMPORTED)
add_library(absl_demangle_rust STATIC IMPORTED)
add_library(absl_exponential_biased STATIC IMPORTED)
add_library(absl_flags_commandlineflag STATIC IMPORTED)
add_library(absl_flags_commandlineflag_internal STATIC IMPORTED)
add_library(absl_flags_config STATIC IMPORTED)
add_library(absl_flags_internal STATIC IMPORTED)
add_library(absl_flags_marshalling STATIC IMPORTED)
add_library(absl_flags_private_handle_accessor STATIC IMPORTED)
add_library(absl_flags_program_name STATIC IMPORTED)
add_library(absl_flags_reflection STATIC IMPORTED)
add_library(absl_graphcycles_internal STATIC IMPORTED)
add_library(absl_hash STATIC IMPORTED)
add_library(absl_hashtablez_sampler STATIC IMPORTED)
add_library(absl_int128 STATIC IMPORTED)
add_library(absl_kernel_timeout_internal STATIC IMPORTED)
add_library(absl_log_severity STATIC IMPORTED)
add_library(absl_low_level_hash STATIC IMPORTED)
add_library(absl_malloc_internal STATIC IMPORTED)
add_library(absl_raw_hash_set STATIC IMPORTED)
add_library(absl_raw_logging_internal STATIC IMPORTED)
add_library(absl_spinlock_wait STATIC IMPORTED)
add_library(absl_stacktrace STATIC IMPORTED)
add_library(absl_str_format_internal STATIC IMPORTED)
add_library(absl_string_view STATIC IMPORTED)
add_library(absl_strings STATIC IMPORTED)
add_library(absl_strings_internal STATIC IMPORTED)
add_library(absl_symbolize STATIC IMPORTED)
add_library(absl_synchronization STATIC IMPORTED)
add_library(absl_throw_delegate STATIC IMPORTED)
add_library(absl_time STATIC IMPORTED)
add_library(absl_time_zone STATIC IMPORTED)
add_library(absl_utf8_for_code_point STATIC IMPORTED)

set_property(TARGET absl_bad_optional_access PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/types/libabsl_bad_optional_access.a")
set_property(TARGET absl_bad_variant_access PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/types/libabsl_bad_variant_access.a")
set_property(TARGET absl_base PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/base/libabsl_base.a")
set_property(TARGET absl_city PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/hash/libabsl_city.a")
set_property(TARGET absl_civil_time PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/time/libabsl_civil_time.a")
set_property(TARGET absl_cord PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/strings/libabsl_cord.a")
set_property(TARGET absl_cord_internal PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/strings/libabsl_cord_internal.a")
set_property(TARGET absl_cordz_functions PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/strings/libabsl_cordz_functions.a")
set_property(TARGET absl_cordz_handle PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/strings/libabsl_cordz_handle.a")
set_property(TARGET absl_cordz_info PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/strings/libabsl_cordz_info.a")
set_property(TARGET absl_crc_cord_state PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/crc/libabsl_crc_cord_state.a")
set_property(TARGET absl_crc_cpu_detect PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/crc/libabsl_crc_cpu_detect.a")
set_property(TARGET absl_crc_internal PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/crc/libabsl_crc_internal.a")
set_property(TARGET absl_crc32c PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/crc/libabsl_crc32c.a")
set_property(TARGET absl_debugging_internal PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/debugging/libabsl_debugging_internal.a")
set_property(TARGET absl_decode_rust_punycode PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/debugging/libabsl_decode_rust_punycode.a")
set_property(TARGET absl_demangle_internal PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/debugging/libabsl_demangle_internal.a")
set_property(TARGET absl_demangle_rust PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/debugging/libabsl_demangle_rust.a")
set_property(TARGET absl_exponential_biased PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/profiling/libabsl_exponential_biased.a")
set_property(TARGET absl_flags_commandlineflag PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/flags/libabsl_flags_commandlineflag.a")
set_property(TARGET absl_flags_commandlineflag_internal PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/flags/libabsl_flags_commandlineflag_internal.a")
set_property(TARGET absl_flags_config PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/flags/libabsl_flags_config.a")
set_property(TARGET absl_flags_internal PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/flags/libabsl_flags_internal.a")
set_property(TARGET absl_flags_marshalling PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/flags/libabsl_flags_marshalling.a")
set_property(TARGET absl_flags_private_handle_accessor PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/flags/libabsl_flags_private_handle_accessor.a")
set_property(TARGET absl_flags_program_name PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/flags/libabsl_flags_program_name.a")
set_property(TARGET absl_flags_reflection PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/flags/libabsl_flags_reflection.a")
set_property(TARGET absl_graphcycles_internal PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/synchronization/libabsl_graphcycles_internal.a")
set_property(TARGET absl_hash PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/hash/libabsl_hash.a")
set_property(TARGET absl_hashtablez_sampler PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/container/libabsl_hashtablez_sampler.a")
set_property(TARGET absl_int128 PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/numeric/libabsl_int128.a")
set_property(TARGET absl_kernel_timeout_internal PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/synchronization/libabsl_kernel_timeout_internal.a")
set_property(TARGET absl_log_severity PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/base/libabsl_log_severity.a")
set_property(TARGET absl_low_level_hash PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/hash/libabsl_low_level_hash.a")
set_property(TARGET absl_malloc_internal PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/base/libabsl_malloc_internal.a")
set_property(TARGET absl_raw_hash_set PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/container/libabsl_raw_hash_set.a")
set_property(TARGET absl_raw_logging_internal PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/base/libabsl_raw_logging_internal.a")
set_property(TARGET absl_spinlock_wait PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/base/libabsl_spinlock_wait.a")
set_property(TARGET absl_stacktrace PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/debugging/libabsl_stacktrace.a")
set_property(TARGET absl_str_format_internal PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/strings/libabsl_str_format_internal.a")
set_property(TARGET absl_string_view PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/strings/libabsl_string_view.a")
set_property(TARGET absl_strings PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/strings/libabsl_strings.a")
set_property(TARGET absl_strings_internal PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/strings/libabsl_strings_internal.a")
set_property(TARGET absl_symbolize PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/debugging/libabsl_symbolize.a")
set_property(TARGET absl_synchronization PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/synchronization/libabsl_synchronization.a")
set_property(TARGET absl_throw_delegate PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/base/libabsl_throw_delegate.a")
set_property(TARGET absl_time PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/time/libabsl_time.a")
set_property(TARGET absl_time_zone PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/time/libabsl_time_zone.a")
set_property(TARGET absl_utf8_for_code_point PROPERTY IMPORTED_LOCATION "${ABSL_LIB_DIR}/debugging/libabsl_utf8_for_code_point.a")

target_link_libraries(
		libabsl INTERFACE

		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_str_format_internal>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_synchronization>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_stacktrace>

		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_bad_optional_access>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_bad_variant_access>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_base>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_city>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_civil_time>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_cord>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_cord_internal>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_cordz_functions>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_cordz_handle>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_cordz_info>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_crc_cord_state>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_crc_cpu_detect>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_crc_internal>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_crc32c>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_debugging_internal>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_decode_rust_punycode>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_demangle_internal>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_demangle_rust>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_exponential_biased>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_flags_commandlineflag>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_flags_commandlineflag_internal>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_flags_config>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_flags_internal>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_flags_marshalling>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_flags_private_handle_accessor>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_flags_program_name>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_flags_reflection>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_graphcycles_internal>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_hash>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_hashtablez_sampler>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_int128>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_kernel_timeout_internal>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_log_severity>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_low_level_hash>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_malloc_internal>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_raw_hash_set>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_raw_logging_internal>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_spinlock_wait>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_string_view>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_strings>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_strings_internal>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_symbolize>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_throw_delegate>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_time>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_time_zone>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,absl_utf8_for_code_point>
)
#endregion abseil
# region re2
set(RE2_DIR "${ONNX_RUNTIME_DEPS_DIR}/re2")
set(RE2_LIB_DIR "${RE2_DIR}/lib")

add_library(libre2 INTERFACE)

add_library(re2 STATIC IMPORTED)

set_property(TARGET re2 PROPERTY IMPORTED_LOCATION "${RE2_LIB_DIR}/libre2.a")

target_link_libraries(
		libre2 INTERFACE

		$<LINK_LIBRARY:WHOLE_ARCHIVE,re2>
)
# endregion re2
# region cpuinfo
set(CPUINFO_DIR "${ONNX_RUNTIME_DEPS_DIR}/cpuinfo")
set(CPUINFO_LIB_DIR "${CPUINFO_DIR}/lib")

add_library(libcpuinfo INTERFACE)

add_library(cpuinfo STATIC IMPORTED)

set_property(TARGET cpuinfo PROPERTY IMPORTED_LOCATION "${CPUINFO_LIB_DIR}/libcpuinfo.a")

target_link_libraries(
		libcpuinfo INTERFACE

		$<LINK_LIBRARY:WHOLE_ARCHIVE,cpuinfo>
)
# endregion cpuinfo

target_link_libraries(
		libonnxruntime INTERFACE

#		onnxruntime_test_utils

		$<LINK_LIBRARY:WHOLE_ARCHIVE,onnx>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,onnx_proto>
#		onnx_test_data_proto
#		onnx_test_runner_common

		$<LINK_LIBRARY:WHOLE_ARCHIVE,onnxruntime_providers>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,onnxruntime_framework>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,onnxruntime_util>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,onnxruntime_common>

		$<LINK_LIBRARY:WHOLE_ARCHIVE,onnxruntime_flatbuffers>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,onnxruntime_graph>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,onnxruntime_mlas>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,onnxruntime_optimizer>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,onnxruntime_session>

		libprotobuf
		libnsync
		libre2
		libabsl
		libcpuinfo
)

target_link_libraries(core PUBLIC libonnxruntime)
target_include_directories(core PUBLIC "${ONNX_RUNTIME_INCLUDE_DIR}")
#endregion onnxruntime
#region ImageMagick
set(IMAGE_MAGICK_DIR "${CMAKE_CURRENT_LIST_DIR}/imagemagick")
set(IMAGE_MAGICK_INCLUDE_DIR "${IMAGE_MAGICK_DIR}/include")
set(IMAGE_MAGICK_LIB_DIR "${IMAGE_MAGICK_DIR}/lib")
set(IMAGE_MAGICK_DEPS_DIR "${IMAGE_MAGICK_DIR}/deps")

add_library(libimagemagick INTERFACE)
add_definitions(-DMAGICKCORE_HDRI_ENABLE=0 -DMAGICKCORE_QUANTUM_DEPTH=8)

# region magick++
add_library(libmagick_pp INTERFACE)

add_library(magick_pp STATIC IMPORTED)

set_property(TARGET magick_pp PROPERTY IMPORTED_LOCATION "${IMAGE_MAGICK_LIB_DIR}/libMagick++.a")

target_link_libraries(
		libmagick_pp INTERFACE

		$<LINK_LIBRARY:WHOLE_ARCHIVE,magick_pp>
)
# endregion magick++
# region magick-core
add_library(libmagick_core INTERFACE)

add_library(magick_core STATIC IMPORTED)

set_property(TARGET magick_core PROPERTY IMPORTED_LOCATION "${IMAGE_MAGICK_LIB_DIR}/libMagickCore.a")

target_link_libraries(
		libmagick_core INTERFACE

		$<LINK_LIBRARY:WHOLE_ARCHIVE,magick_core>
)
# endregion magick-core
# region magick-wand
add_library(libmagick_wand INTERFACE)

add_library(magick_wand STATIC IMPORTED)

set_property(TARGET magick_wand PROPERTY IMPORTED_LOCATION "${IMAGE_MAGICK_LIB_DIR}/libMagickWand.a")

target_link_libraries(
		libmagick_wand INTERFACE

		$<LINK_LIBRARY:WHOLE_ARCHIVE,magick_wand>
)
# endregion magick-wand

# region raw
set(RAW_DIR "${IMAGE_MAGICK_DEPS_DIR}/raw")
set(RAW_LIB_DIR "${RAW_DIR}/lib")

add_library(libraw INTERFACE)

add_library(raw STATIC IMPORTED)

set_property(TARGET raw PROPERTY IMPORTED_LOCATION "${RAW_LIB_DIR}/libraw_r.a")

target_link_libraries(
		libraw INTERFACE

		$<LINK_LIBRARY:WHOLE_ARCHIVE,raw>
)
# endregion raw
# region png
set(PNG_DIR "${IMAGE_MAGICK_DEPS_DIR}/png")
set(PNG_LIB_DIR "${PNG_DIR}/lib")

add_library(libpng INTERFACE)

add_library(png STATIC IMPORTED)

set_property(TARGET png PROPERTY IMPORTED_LOCATION "${PNG_LIB_DIR}/libpng.a")

target_link_libraries(
		libpng INTERFACE

		$<LINK_LIBRARY:WHOLE_ARCHIVE,png>
)
# endregion png
# region jpeg
set(JPEG_DIR "${IMAGE_MAGICK_DEPS_DIR}/jpeg")
set(JPEG_LIB_DIR "${JPEG_DIR}/lib")

add_library(libjpeg INTERFACE)

add_library(jpeg STATIC IMPORTED)

set_property(TARGET jpeg PROPERTY IMPORTED_LOCATION "${JPEG_LIB_DIR}/libjpeg.a")

target_link_libraries(
		libjpeg INTERFACE

		$<LINK_LIBRARY:WHOLE_ARCHIVE,jpeg>
)
# endregion jpeg
# region tiff
set(TIFF_DIR "${IMAGE_MAGICK_DEPS_DIR}/tiff")
set(TIFF_LIB_DIR "${TIFF_DIR}/lib")

add_library(libtiff INTERFACE)

add_library(tiff STATIC IMPORTED)

set_property(TARGET tiff PROPERTY IMPORTED_LOCATION "${TIFF_LIB_DIR}/libtiff.a")

target_link_libraries(
		libtiff INTERFACE

		$<LINK_LIBRARY:WHOLE_ARCHIVE,tiff>
)
# endregion tiff
# region lcms2
set(LCMS_DIR "${IMAGE_MAGICK_DEPS_DIR}/lcms2")
set(LCMS_LIB_DIR "${LCMS_DIR}/lib")

add_library(liblcms2 INTERFACE)

add_library(lcms2 STATIC IMPORTED)
add_library(lcms2_fast_float STATIC IMPORTED)
add_library(lcms2_threaded STATIC IMPORTED)

set_property(TARGET lcms2 PROPERTY IMPORTED_LOCATION "${LCMS_LIB_DIR}/liblcms2.a")
set_property(TARGET lcms2_fast_float PROPERTY IMPORTED_LOCATION "${LCMS_LIB_DIR}/liblcms2_fast_float.a")
set_property(TARGET lcms2_threaded PROPERTY IMPORTED_LOCATION "${LCMS_LIB_DIR}/liblcms2_threaded.a")

target_link_libraries(
		liblcms2 INTERFACE

		$<LINK_LIBRARY:WHOLE_ARCHIVE,lcms2>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,lcms2_fast_float>
		$<LINK_LIBRARY:WHOLE_ARCHIVE,lcms2_threaded>
)
# endregion lcms2

target_link_libraries(
		libimagemagick INTERFACE

		libmagick_pp
		libmagick_core
		libmagick_wand

		libraw
		libpng
		libjpeg
		libtiff
		liblcms2
)

#target_include_directories(core PUBLIC
#		"${IMAGE_MAGICK_INCLUDE_DIR}"
#		"${LCMS_INCLUDE_DIR}"
#)
#endregion ImageMagick
# region core

find_package(ZLIB REQUIRED)
find_package(OpenMP REQUIRED)

target_link_libraries(core PUBLIC

		libonnxruntime
		libimagemagick

		${ZLIB_LIBRARIES}
		OpenMP::OpenMP_CXX
)

target_include_directories(core PUBLIC
		${IMAGE_MAGICK_INCLUDE_DIR}
)
# endregion core

#region Java
if (NOT DEFINED JAVA_ROOT)
	message(FATAL_ERROR "JAVA_ROOT is not defined")
endif ()

target_include_directories(core PRIVATE "${JAVA_ROOT}/include/")
target_include_directories(core PRIVATE "${JAVA_ROOT}/include/linux")
#endregion Java

set_target_properties(core PROPERTIES LINK_FLAGS "-Wl,-rpath,.")

#add_dependencies(exec core)
#target_link_libraries(exec PRIVATE core)
