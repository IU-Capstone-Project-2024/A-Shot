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
}

#endif //CORE_UTIL_HH
