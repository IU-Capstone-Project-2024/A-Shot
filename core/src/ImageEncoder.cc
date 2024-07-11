//
// Created by a on 7/10/24.
//

#include "ImageEncoder.hh"

ImageEncoder::ImageEncoder(
	Exhaust<ImageBlur> &input,
	Drain<ImageBlurEmbedding> &output
) :
	PipelineStep(input, output) {
	env = Ort::Env(ORT_LOGGING_LEVEL_WARNING, "ImageEncoder");
	memory_info = Ort::MemoryInfo::CreateCpu(OrtDeviceAllocator, OrtMemTypeDefault);

	Ort::SessionOptions session_options;
	session = Ort::Session(env, EMBEDDING_MODEL_PATH, session_options);
}

void ImageEncoder::process(ImageBlur &input) {

}
