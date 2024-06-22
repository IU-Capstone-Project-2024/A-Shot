#include "Native.h"

JNIEXPORT jint JNICALL Java_Native_hello(JNIEnv *env, jclass cls) {
	return 42;
}
