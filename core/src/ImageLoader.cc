//
// Created by a on 6/21/24.
//

#include <filesystem>
#include <utility>
#include "ImageLoader.hh"

ImageLoader::ImageLoader(
	Exhaust<std::string> &input,
	Drain<Magick::Image> &output,
	const std::function<bool(const std::string &path)> &filter
) :
	PipelineStep(input, output),
	filter(filter) {
}

void ImageLoader::load_image(const std::string &path) {
	try {
		if (filter(path)) {
			Magick::Image image(path);
			image.alpha(false);
			image.colorSpace(MagickCore::sRGBColorspace);

			output.flush(std::move(image), true);
		}
	} catch (const Magick::Exception &e) {
		// TODO: handle
	}
}

void ImageLoader::process(std::string &path) {
	if (std::filesystem::is_directory(path)) {
		for (const auto &entry: std::filesystem::directory_iterator(path)) {
			load_image(entry.path());
		}
	}
	else {
		load_image(path);
	}
}