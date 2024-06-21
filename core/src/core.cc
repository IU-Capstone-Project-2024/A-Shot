#include "core.hh"
#include "BlurDetector.hh"
#include "ImageLoader.hh"

#include <iostream>
#include <filesystem>
#include <Magick++.h>

//#include "BlurDetector.h"

void hello() {
	std::cout << "Hello, World!" << std::endl;
	Pipe<std::string> input(1);
	Pipe<Magick::Image> loader_detector(1);
	Pipe<Magick::Image> output(1);

	ImageLoader loader(input, loader_detector);
	BlurDetector detector(loader_detector, output);

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
		const auto &path = entry.path();
		Magick::Image image;
		if (input.flush(path) && output.sink(image)) {
			image.write("results/" + path.stem().string() + ".jpg");
		}
	}
}
