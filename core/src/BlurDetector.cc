//
// Created by a on 6/20/24.
//

#include "BlurDetector.hh"

BlurDetector::BlurDetector(
	Exhaust<Magick::Image> &input,
	Drain<ImageBlur> &output
) :
	PipelineStep(input, output) {
	env = Ort::Env(ORT_LOGGING_LEVEL_WARNING, "BlurDetector");
	memory_info = Ort::MemoryInfo::CreateCpu(OrtDeviceAllocator, OrtMemTypeDefault);

	Ort::SessionOptions session_options;
	session = Ort::Session(env, BLUR_MODEL_PATH, session_options);
}

void BlurDetector::process(Magick::Image &input) {
	Magick::Geometry geometry(INPUT_WIDTH, INPUT_HEIGHT);
	geometry.aspect(true);

	Magick::Image image(input);
	image.filterType(MagickCore::TriangleFilter);
	image.resize(geometry);

	std::vector<float> input_tensor;
	util::MagickToTensor(image, {0.485f, 0.456f, 0.406f}, {0.229f, 0.224f, 0.225f}, input_tensor);

	Ort::Allocator allocator{session, memory_info};
	auto input_name = session.GetInputNameAllocated(0, allocator);
	auto output_name = session.GetOutputNameAllocated(0, allocator);

	int64_t shape[4] = {1, INPUT_DEPTH, INPUT_WIDTH, INPUT_HEIGHT};
	Ort::Value inputs[1] = {
		Ort::Value::CreateTensor<float>(memory_info, input_tensor.data(), input_tensor.size(), shape, 4)
	};

	Ort::RunOptions run_options;
	const char *input_names[1]{input_name.get()};
	const char *output_names[1]{output_name.get()};
	std::vector<Ort::Value> outputs = session.Run(run_options, input_names, inputs, 1, output_names, 1);

	const Ort::Value &output_value = outputs[0];
	const auto *data = output_value.GetTensorData<float>();
	std::vector<float> output_tensor(data, data + INPUT_HEIGHT * INPUT_WIDTH);

//	Magick::Image result;
//	util::TensorToMagick(output_tensor, INPUT_WIDTH, INPUT_HEIGHT, "K", result);
	float avg = std::accumulate(output_tensor.begin(), output_tensor.end(), 0.0f);
	avg /= (float) output_tensor.size();

	output.flush({input, avg}, true);
}
