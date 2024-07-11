//
// Created by a on 6/21/24.
//

#include <filesystem>
#include "ImageLoader.hh"

ImageLoader::ImageLoader(
	Exhaust<std::string> &input,
	Drain<Magick::Image> &output
) :
	PipelineStep(input, output) {
}

void ImageLoader::load_image(const std::string &path) {
	try {
		Magick::Image image(path);
		output.flush(std::move(image), true);
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