#include "jni_core.hh"

struct JNI JNI;

JNIEXPORT jint JNICALL JNI_OnLoad(
	JavaVM *vm,
	void *reserved
) {
	JNIEnv *env;
	if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
		return -1;
	}

	std::cout << "Hello, World!" << std::endl;
	JNI.vm = vm;

	JNI.ImageBlur.cls = (jclass) env->NewGlobalRef(env->FindClass("core/src/jni/LoadingPipeline$ImageBlur"));
	JNI.ImageBlur.inst = env->GetMethodID(JNI.ImageBlur.cls, "<init>", "(Ljava/lang/String;F)V");

	return JNI_VERSION_1_6;
}

JNIEXPORT void JNICALL JNI_OnUnload(
	JavaVM *vm,
	void *reserved
) {
	std::cout << "Bye, World!" << std::endl;
}


JNIEXPORT jint JNICALL Java_core_src_jni_Core_hello(
	JNIEnv *env,
	jclass cls
) {
	std::cout << "Core.hello()" << std::endl;
	return 42;
}
