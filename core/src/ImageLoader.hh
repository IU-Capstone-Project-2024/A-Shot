//
// Created by a on 6/21/24.
//

#ifndef CORE_IMAGE_LOADER_HH
#define CORE_IMAGE_LOADER_HH


#include <string>
#include <Magick++/Image.h>
#include <condition_variable>
#include <thread>
#include <mutex>
#include <optional>
#include <queue>

#include "PipelineStep.hh"
#include "Pipe.hh"

class ImageLoader : PipelineStep<std::string, Magick::Image> {
private:

	std::thread worker;

	void process(const std::string &path);

	void run();

public:
	explicit ImageLoader(
		Pipe<std::string> &input,
		Pipe<Magick::Image> &output
	);

	~ImageLoader();
};


#endif //CORE_IMAGE_LOADER_HH
