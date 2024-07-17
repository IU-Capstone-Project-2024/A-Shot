//
// Created by a on 6/21/24.
//

#include <Magick++/Image.h>
#include "LoadingPipeline.hh"

LoadingPipeline::LoadingPipeline(
	const char *blur_model_path,
	const char *encoder_model_path,
	const std::function<bool(const std::string &path)> &filter
) :
	image_loader(path_pipe, image_pipe, filter),
	blur_detector(image_pipe, blur_pipe, blur_model_path),
	image_encoder(blur_pipe, embedding_pipe, encoder_model_path) {

}

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
	jobject instance,
	jstring j_bm_path,
	jstring j_em_path
) {
	instance = env->NewGlobalRef(instance);
	auto filter = [instance](const std::string &path) {
		JNIEnv *env;
		JNI.vm->AttachCurrentThread((void **) &env, nullptr);

		jstring j_path = env->NewStringUTF(path.c_str());
		return (bool) env->CallBooleanMethod(instance, JNI.LoadingPipeline.filter, j_path);
	};

	jboolean bm_copy;
	jboolean em_copy;

	const char *bm_path = env->GetStringUTFChars(j_bm_path, &bm_copy);
	const char *em_path = env->GetStringUTFChars(j_em_path, &em_copy);

	jlong pipeline = (jlong) new LoadingPipeline(bm_path, em_path, filter);

	if (bm_copy) env->ReleaseStringUTFChars(j_bm_path, bm_path);
	if (bm_copy) env->ReleaseStringUTFChars(j_em_path, em_path);

	return pipeline;
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

JNIEXPORT jobject JNICALL Java_core_LoadingPipeline_nSuck(
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
	jfloat blur = result.blur;

	jbyteArray embedding = env->NewByteArray(result.embedding.size() * (sizeof(float) / sizeof(jbyte)));
	env->SetByteArrayRegion(embedding, 0, (jsize) result.embedding.size(), (jbyte *) result.embedding.data());

	Magick::Blob blob;
	result.image.write(&blob, "JPEG");

	jbyteArray thumbnail = env->NewByteArray((jsize) blob.length());
	env->SetByteArrayRegion(thumbnail, 0, (jsize) blob.length(), (jbyte *) blob.data());

	return env->NewObject(JNI.Result.cls, JNI.Result.inst, path, blur, embedding, thumbnail);
}
