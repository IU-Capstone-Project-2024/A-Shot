//
// Created by a on 6/20/24.
//

#include "BlurDetector.hh"

BlurDetector::BlurDetector(
	SupplyPipe<Magick::Image> &input,
	DrainPipe<ImageBlur> &output
) :
	PipelineStep(input, output),
	worker(&BlurDetector::run, this) {
	env = Ort::Env(ORT_LOGGING_LEVEL_WARNING, "BlurDetector");
	memory_info = Ort::MemoryInfo::CreateCpu(OrtDeviceAllocator, OrtMemTypeDefault);

	Ort::SessionOptions session_options;
	session = Ort::Session(env, MODEL, session_options);
}

void BlurDetector::run() {
	for (Magick::Image image; !input.sink(image, true);) {
		try {
			process(image);
		} catch (const std::exception &e) {
			std::cout << e.what() << std::endl;
		}
	}
	input.close();
	output.close();
}

void BlurDetector::process(Magick::Image &input) {
	Magick::Geometry geometry(INPUT_WIDTH, INPUT_HEIGHT);
	geometry.aspect(true);

	input.colorSpace(MagickCore::sRGBColorspace);
	input.filterType(MagickCore::TriangleFilter);
	input.resize(geometry);

	std::vector<float> input_tensor;
	util::MagickToTensor(input, {0.485f, 0.456f, 0.406f}, {0.229f, 0.224f, 0.225f}, input_tensor);

	Ort::Allocator allocator{session, memory_info};
	auto input_name = session.GetInputNameAllocated(0, allocator);
	auto output_name = session.GetOutputNameAllocated(0, allocator);

	int64_t shape[4] = {1, INPUT_DEPTH, INPUT_WIDTH, INPUT_HEIGHT};
	Ort::Value inputs[] = {
		Ort::Value::CreateTensor<float>(memory_info, input_tensor.data(), INPUT_LENGTH, shape, 4)
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
	avg = avg / (float) output_tensor.size();

	output.flush(std::make_pair(input, avg), true);
}

BlurDetector::~BlurDetector() {
	input.close();
	output.close();
	worker.join();
}
