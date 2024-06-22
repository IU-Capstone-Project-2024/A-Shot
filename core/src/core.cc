#include "core.hh"
#include "BlurDetector.hh"
#include "ImageLoader.hh"
#include "jni/LoadingPipeline.hh"

#include <iostream>
#include <filesystem>
#include <Magick++.h>

void hello() {
	std::cout << "Hello, World!" << std::endl;

	LoadingPipeline pipeline;
	pipeline.flush("/home/a/Pictures/Screenshots", true);
	pipeline.dry();

	for (ImageBlur item; !pipeline.suck(item, true);) {
		std::cout << item.first.fileName() << ' ' << item.second << std::endl;
	}
}
