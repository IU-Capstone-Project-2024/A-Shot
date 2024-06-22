#include "Core.h"

struct JNIBinding {
	JavaVM *vm;

	struct {
		jclass cls;
		jmethodID sink;
	} LoadingPipeline;
} jni;

JNIEXPORT jint JNICALL JNI_OnLoad(
	JavaVM *vm,
	void *reserved
) {
	JNIEnv *env;
	if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
		return -1;
	}

	std::cout << "Hello, World!" << std::endl;
	jni.vm = vm;

	jni.LoadingPipeline.cls = (jclass) env->NewGlobalRef(env->FindClass("core/src/jni/LoadingPipeline"));
	jni.LoadingPipeline.sink = env->GetMethodID(jni.LoadingPipeline.cls, "sink", "(Ljava/lang/String;F)V");

	return JNI_VERSION_1_6;
}


JNIEXPORT jint JNICALL Java_core_src_jni_Core_hello(
	JNIEnv *env,
	jclass cls,
	jstring jpath
) {
	jboolean copy;
	const char *path = env->GetStringUTFChars(jpath, &copy);

	/*Magick::Image image;
	image.read(path);

	BlurDetector detector;
	detector.Run(image);

	image.write("AAA.jpg");*/

	if (copy) {
		env->ReleaseStringUTFChars(jpath, path);
	}
	return 42;
}
