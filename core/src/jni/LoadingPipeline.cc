//
// Created by a on 6/21/24.
//

#include <Magick++/Image.h>
#include "LoadingPipeline.hh"
#include "../Pipe.hh"
#include "../BlurDetector.hh"
#include "../ImageLoader.hh"

JNIEXPORT jlong JNICALL Java_core_src_jni_LoadingPipeline_nNew(
	JNIEnv *env,
	jobject j_this
) {
	return 0;
}

LoadingPipeline::LoadingPipeline(jobject j_this) : j_this(j_this) {
	Pipe<std::string> input(1);
	Pipe<Magick::Image> loader_detector(1);
	Pipe<ImageBlur> output(1);

	ImageLoader loader(input, loader_detector);
	BlurDetector detector(loader_detector, output);

	std::vector<std::filesystem::directory_entry> dir_entries;
	for (const auto &entry: std::filesystem::directory_iterator(
		"../utils/src/blur_filtering/samples/")) {
		if (entry.is_directory()) {
			continue;
		}

		const auto &path = entry.path();
		ImageBlur image;
		if (!input.flush(path, true) && !output.sink(image, true)) {
//			image.write("results/" + path.stem().string() + ".jpg");
			std::cout << image.first.fileName() << " " << 1 - image.second << std::endl;
		}
	}
}
