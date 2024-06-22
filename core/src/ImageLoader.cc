//
// Created by a on 6/21/24.
//

#include "ImageLoader.hh"

ImageLoader::ImageLoader(
	SupplyPipe<std::string> &input,
	DrainPipe<Magick::Image> &output
) :
	PipelineStep(input, output),
	worker(&ImageLoader::run, this) {
}

void ImageLoader::process(const std::string &path) {
	try {
		Magick::Image image(path);
		output.flush(std::move(image), true);
	} catch (const Magick::Exception &e) {
		// TODO: handle
	}
}

void ImageLoader::run() {
	for (std::string path; !input.sink(path, true);) {
		process(path);
	}
	input.close();
	output.close();
}

ImageLoader::~ImageLoader() {
	input.close();
	output.close();
	worker.join();
}
