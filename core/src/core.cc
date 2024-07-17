#include "core.hh"
#include "BlurDetector.hh"
#include "ImageLoader.hh"
#include "jni/LoadingPipeline.hh"

#include <iostream>
#include <filesystem>
#include <Magick++.h>

void hello(std::string &&path) {
	std::cout << "Hello, World!" << std::endl;

	LoadingPipeline pipeline(
		"/home/a/Projects/A-Shot/core/checkpoints/D-DFFNet.ort",
		"/home/a/Projects/A-Shot/core/checkpoints/CVNet50.ort",
		[](const std::string &) { return true; }
	);
	pipeline.flush(std::move(path), true);
	pipeline.dry();

	for (ImageBlurEmbedding item; !pipeline.suck(item, true);) {
		std::cout
			<< item.image.fileName() << ' '
			<< item.blur << ' '
			<< item.embedding[0] << "..."
			<< item.embedding[EMBEDDING_SIZE - 1]
			<< std::endl;
	}

	/*
	pipeline.flush("...", true);
	pipeline.flush("...", true);
	pipeline.flush("...", true);
	pipeline.dry();

	ImageBlurEmbedding a, b, c;
	pipeline.suck(a, true);
	pipeline.suck(b, true);
	pipeline.suck(c, true);
	pipeline.plug();

	std::cout
		<< util::CosineSimilarity(a.embedding, b.embedding) << ' '
		<< util::CosineSimilarity(a.embedding, c.embedding) << ' '
		<< util::CosineSimilarity(b.embedding, c.embedding)
		<< std::endl;
	*/
}
