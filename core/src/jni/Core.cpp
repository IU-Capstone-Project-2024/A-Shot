#include "Core.h"
#include "../core.h"

JNIEXPORT jint JNICALL Java_core_src_jni_Core_hello(
		JNIEnv *env,
		jclass cls,
		jstring jpath
) {
	jboolean copy;
	const char *path = env->GetStringUTFChars(jpath, &copy);

	hello(path);

	if (copy) {
		env->ReleaseStringUTFChars(jpath, path);
	}
	return 42;
}
