//
// Created by a on 6/20/24.
//

#ifndef CORE_BLUR_DETECTOR_HH
#define CORE_BLUR_DETECTOR_HH

#include <thread>
#include <Magick++/Image.h>

#undef IsNaN

#include <onnxruntime_cxx_api.h>
#include <Magick++/Functions.h>
#include <Magick++/STL.h>
#include <filesystem>
#include <numeric>
#include <iostream>

#include "Util.hh"
#include "PipelineStep.hh"

using ImageBlur = std::pair<Magick::Image, float>;

class BlurDetector : PipelineStep<Magick::Image, ImageBlur> {
private:
	const char *MODEL = "checkpoints/D-DFFNet.ort";

	static const int INPUT_WIDTH = 320;
	static const int INPUT_HEIGHT = 320;
	static const int INPUT_DEPTH = 3;

	static constexpr int INPUT_LENGTH = INPUT_WIDTH * INPUT_HEIGHT * INPUT_DEPTH;

	Ort::Env env{nullptr};
	Ort::Session session{nullptr};
	Ort::MemoryInfo memory_info{nullptr};

	std::thread worker;

	void process(Magick::Image &input);

	void run();

public:
	BlurDetector(
		SupplyPipe<Magick::Image> &input,
		DrainPipe<ImageBlur> &output
	);

	~BlurDetector();
};


#endif //CORE_BLUR_DETECTOR_HH
