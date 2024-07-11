//
// Created by a on 7/10/24.
//

#ifndef CORE_IMAGE_ENCODER_HH
#define CORE_IMAGE_ENCODER_HH

#include "BlurDetector.hh"

#include <optional>
#include <tuple>

#define EMBEDDING_SIZE 2048

struct ImageBlurEmbedding : ImageBlur {
	std::array<float, EMBEDDING_SIZE> embedding{};
};

class ImageEncoder : PipelineStep<ImageBlur, ImageBlurEmbedding> {
private:
	Ort::Env env{nullptr};
	Ort::Session session{nullptr};
	Ort::MemoryInfo memory_info{nullptr};

	std::thread worker;

	void process(ImageBlur &input) override;

public:
	ImageEncoder(
		Exhaust<ImageBlur> &input,
		Drain<ImageBlurEmbedding> &output
	);
};

#endif //CORE_IMAGE_ENCODER_HH
