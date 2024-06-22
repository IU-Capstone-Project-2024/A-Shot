//
// Created by a on 6/21/24.
//

#include <filesystem>
#include "ImageLoader.hh"

ImageLoader::ImageLoader(
	Exhaust<std::string> &input,
	Drain<Magick::Image> &output
) :
	PipelineStep(input, output),
	worker(&ImageLoader::run, this) {
}

void ImageLoader::load_image(const std::string &path) {
	try {
		Magick::Image image(path);
		output.flush(std::move(image), true);
	} catch (const Magick::Exception &e) {
		// TODO: handle
	}
}


void ImageLoader::process(const std::string &path) {
	if (std::filesystem::is_directory(path)) {
		for (const auto &entry: std::filesystem::directory_iterator(path)) {
			load_image(entry.path());
		}
	}
	else {
		load_image(path);
	}
}

void ImageLoader::run() {
	for (std::string path; !input.suck(path, true);) {
		process(path);
	}
	input.plug();
	output.dry();
}

ImageLoader::~ImageLoader() {
	input.plug();
	output.dry();
	worker.join();
}
