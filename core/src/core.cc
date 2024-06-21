#include "core.hh"
#include "BlurDetector.hh"

#include <iostream>
#include <filesystem>
#include <Magick++.h>

//#include "BlurDetector.h"

void hello() {
	std::cout << "Hello, World!" << std::endl;
	BlurDetector detector;

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
		try {
			const auto &path = entry.path();

			Magick::Image input, output;
			input.read(path);

			detector.Run(input, output);
			output.write("results/" + path.stem().string() + ".jpg");
		} catch (const Magick::Exception &error) {

		}
	}
}
