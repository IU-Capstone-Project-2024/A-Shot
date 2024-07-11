//
// Created by a on 6/21/24.
//

#include <Magick++/Image.h>
#include "LoadingPipeline.hh"

PipeResult LoadingPipeline::flush(std::string &&item, bool block) {
	return path_pipe.flush(std::move(item), false);
}

PipeResult LoadingPipeline::suck(ImageBlurEmbedding &item, bool block) {
	return embedding_pipe.suck(item, block);
}

void LoadingPipeline::dry() {
	path_pipe.dry();
}

void LoadingPipeline::plug() {
	blur_pipe.plug();
}

[[maybe_unused]] JNIEXPORT jlong JNICALL Java_core_LoadingPipeline_nNew(
	JNIEnv *env,
	jobject _
) {
	return (jlong) new LoadingPipeline();
}

JNIEXPORT jboolean JNICALL Java_core_LoadingPipeline_nFlush(
	JNIEnv *env,
	jobject _,
	jlong ptr,
	jstring j_path
) {
	jboolean is_copy;
	const char *path = env->GetStringUTFChars(j_path, &is_copy);

	auto *pipeline = (LoadingPipeline *) ptr;
	bool result = !pipeline->flush(std::string(path), false);

	if (is_copy) {
		env->ReleaseStringUTFChars(j_path, path);
	}

	return result;
}

JNIEXPORT jobject JNICALL Java_core_LoadingPipeline_nSink(
	JNIEnv *env,
	jobject _,
	jlong ptr
) {
	auto *pipeline = (LoadingPipeline *) ptr;

	ImageBlurEmbedding result;
	if (pipeline->suck(result, true)) {
		// TODO: return something else on CLOG
		return nullptr;
	}

	jstring path = env->NewStringUTF(result.image.fileName().c_str());
	jfloat score = result.blur;

	return env->NewObject(JNI.ImageBlur.cls, JNI.ImageBlur.inst, path, score);
}
