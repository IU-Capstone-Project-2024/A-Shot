#include <jni.h>
#include <iostream>

#ifndef CORE_H
#define CORE_H
#ifdef __cplusplus
extern "C" {
#endif

extern struct JNIBinding jni;

JNIEXPORT jint JNICALL JNI_OnLoad(
	JavaVM *vm,
	void *reserved
);

JNIEXPORT jint JNICALL Java_core_src_jni_Core_hello
	(JNIEnv *, jclass, jstring);


#ifdef __cplusplus
}
#endif
#endif
