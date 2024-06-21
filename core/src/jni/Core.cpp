#include "Core.h"
#include "../BlurDetector.hh"

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
