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
	Magick::Image &image = input.image;
	image.alpha(false);
	image.colorSpace(MagickCore::sRGBColorspace);

	std::vector<float> input_tensor;
	util::MagickToTensor(image, {0.485f, 0.456f, 0.406f}, {0.229f, 0.224f, 0.225f}, input_tensor);

	Ort::Allocator allocator{session, memory_info};
	auto input_name = session.GetInputNameAllocated(0, allocator);
	auto output_name = session.GetOutputNameAllocated(0, allocator);

	int64_t shape[4] = {1, (int64_t) image.channels(), (int64_t) image.columns(), (int64_t) image.rows()};
	Ort::Value inputs[1] = {
		Ort::Value::CreateTensor<float>(memory_info, input_tensor.data(), input_tensor.size(), shape, 4)
	};

	Ort::RunOptions run_options;
	const char *input_names[1]{input_name.get()};
	const char *output_names[1]{output_name.get()};
	std::vector<Ort::Value> outputs = session.Run(run_options, input_names, inputs, 1, output_names, 1);

	const Ort::Value &output_value = outputs[0];
	const auto *data = output_value.GetTensorData<float>();

	std::array<float, EMBEDDING_SIZE> embedding{};
	std::copy(data, data + EMBEDDING_SIZE, embedding.begin());

	output.flush({input.image, input.blur, embedding}, true);
}
