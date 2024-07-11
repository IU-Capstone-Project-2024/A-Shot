//
// Created by a on 6/21/24.
//

#ifndef CORE_UTIL_HH
#define CORE_UTIL_HH

#include <vector>
#include <Magick++/Image.h>
#include <Magick++/STL.h>


namespace util {
	void MagickToTensor(
		Magick::Image &image,
		const std::vector<float> &mean,
		const std::vector<float> &stddev,
		std::vector<float> &tensor
	);

	void TensorToMagick(
		const std::vector<float> &tensor,
		size_t width,
		size_t height,
		const std::string &channels,
		Magick::Image &image,
		Magick::ColorspaceType colorspace = Magick::ColorspaceType::sRGBColorspace
	);

	template<typename Container>
	float Dot(
		const Container &a,
		const Container &b
	) {
		if (a.size() != b.size()) {
			throw std::invalid_argument("a and b should be the same length");
		}

		if (a.empty()) {
			throw std::invalid_argument("0-dim vectors");
		}

		float result = 0;
		for (int i = 0; i < a.size(); ++i) {
			result += a[i] * b[i];
		}

		return result;
	}

	template<typename Container>
	float EuclideanNorm(
		const Container &v
	) {
		float norm = 0;
		for (float i: v) {
			norm += i * i;
		}
		return norm;
	}

	template<typename Container>
	float CosineSimilarity(
		const Container &a,
		const Container &b
	) {
		return Dot(a, b) / (EuclideanNorm(a) * EuclideanNorm(b));
	}

}

#endif //CORE_UTIL_HH
