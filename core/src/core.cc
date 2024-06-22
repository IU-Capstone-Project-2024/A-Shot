#include "core.hh"
#include "BlurDetector.hh"
#include "ImageLoader.hh"

#include <iostream>
#include <filesystem>
#include <Magick++.h>

//#include "BlurDetector.h"

void hello() {
	std::cout << "Hello, World!" << std::endl;
	Pipe<std::string> path_pipe(1);
	Pipe<Magick::Image> image_pipe(1);
	Pipe<ImageBlur> blur_pipe(1);

	ImageLoader loader(path_pipe, image_pipe);
	BlurDetector detector(image_pipe, blur_pipe);

	std::vector<std::filesystem::directory_entry> dir_entries;
	for (const auto &entry: std::filesystem::directory_iterator(
		"../utils/src/blur_filtering/samples/")) {
		if (entry.is_directory()) {
			continue;
		}
		dir_entries.push_back(entry);
	}

	std::sort(
		dir_entries.begin(), dir_entries.end(),
		[](const auto &a, const auto &b) {
			return a.path().stem() < b.path().stem();
		}
	);

	for (const auto &entry: dir_entries) {
		ImageBlur image;
		if (!path_pipe.flush(entry.path(), true) && !blur_pipe.sink(image, true)) {
			std::cout << image.first.fileName() << " " << 1 - image.second << std::endl;
		}
	}
}
