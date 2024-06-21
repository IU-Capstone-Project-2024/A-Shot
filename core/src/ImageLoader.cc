//
// Created by a on 6/21/24.
//

#include "ImageLoader.hh"

ImageLoader::ImageLoader(
	Pipe<std::string> &input,
	Pipe<Magick::Image> &output
) :
	PipelineStep(input, output),
	worker(&ImageLoader::run, this) {
}

void ImageLoader::process(const std::string &path) {
	try {
		Magick::Image image(path);
		output.flush(std::move(image));
	} catch (const Magick::Exception &e) {
		// TODO: handle
	}
}

void ImageLoader::run() {
	for (std::string path; input.sink(path);) {
		process(path);
	}
	output.flush(nullptr);
}

ImageLoader::~ImageLoader() {
	// FIXME: closing input will close it for everyone
	input.close();
	worker.join();
	output.close();
}
