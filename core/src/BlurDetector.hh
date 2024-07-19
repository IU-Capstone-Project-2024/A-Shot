//
// Created by a on 6/20/24.
//

#ifndef CORE_BLUR_DETECTOR_HH
#define CORE_BLUR_DETECTOR_HH

#include <thread>
#include <Magick++/Image.h>

#undef IsNaN

#include <onnxruntime/core/session/onnxruntime_cxx_api.h>
#include <Magick++/Functions.h>
#include <Magick++/STL.h>
#include <filesystem>
#include <numeric>
#include <iostream>

#include "Util.hh"
#include "PipelineStep.hh"

struct ImageBlur {
	Magick::Image image;
	float blur;
};

class BlurDetector : PipelineStep<Magick::Image, ImageBlur> {
private:

	static const int INPUT_WIDTH = 320;
	static const int INPUT_HEIGHT = 320;
	static const int INPUT_DEPTH = 3;

	Ort::Env env{nullptr};
	Ort::Session session{nullptr};
	Ort::MemoryInfo memory_info{nullptr};

	std::thread worker;

	void process(Magick::Image &input) override;

public:
	BlurDetector(
		Exhaust<Magick::Image> &input,
		Drain<ImageBlur> &output,
		const char *model_path
	);
};


#endif //CORE_BLUR_DETECTOR_HH
