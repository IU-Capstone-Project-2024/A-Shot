#include <jni.h>
#include <iostream>

#ifndef JNI_CORE_HH
#define JNI_CORE_HH
#ifdef __cplusplus
extern "C" {
#endif

#define JNIKEEP __attribute__((used))

struct JNI {
	JavaVM *vm;

	struct {
		jclass cls;
		jmethodID inst;
	} ImageBlur;
};

extern JNI JNI;

JNIEXPORT jint JNICALL Java_core_Core_hello(
	JNIEnv *,
	jclass
) JNIKEEP;


JNIEXPORT jlong JNICALL Java_core_LoadingPipeline_nNew(
	JNIEnv *env,
	jobject _
) JNIKEEP;

JNIEXPORT jboolean JNICALL Java_core_LoadingPipeline_nFlush(
	JNIEnv *env,
	jobject _,
	jlong ptr,
	jstring j_path
) JNIKEEP;

JNIEXPORT jobject JNICALL Java_core_LoadingPipeline_nSink(
	JNIEnv *env,
	jobject _,
	jlong ptr
) JNIKEEP;

#ifdef __cplusplus
}
#endif
#endif
