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

	JNI.LoadingPipeline.cls = (jclass) env->NewGlobalRef(env->FindClass("core/LoadingPipeline"));
	JNI.LoadingPipeline.filter = env->GetMethodID(JNI.LoadingPipeline.cls, "filter", "(Ljava/lang/String;)Z");

	JNI.Result.cls = (jclass) env->NewGlobalRef(env->FindClass("core/LoadingPipeline$Result"));
	JNI.Result.inst = env->GetMethodID(JNI.Result.cls, "<init>", "(Ljava/lang/String;F[B[B)V");

	return JNI_VERSION_1_6;
}

JNIEXPORT void JNICALL JNI_OnUnload(
	JavaVM *vm,
	void *reserved
) {
	std::cout << "Bye, World!" << std::endl;
}


JNIEXPORT jint JNICALL Java_core_Core_hello(
	JNIEnv *env,
	jclass cls
) {
	std::cout << "Core.hello()" << std::endl;
	return 42;
}
