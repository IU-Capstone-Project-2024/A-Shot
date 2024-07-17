//
// Created by a on 6/21/24.
//

#ifndef CORE_LOADING_PIPELINE_HH
#define CORE_LOADING_PIPELINE_HH
/*#ifdef __cplusplus
extern "C" {
#endif*/

#include "jni_core.hh"
#include "../Pipe.hh"
#include "../ImageLoader.hh"
#include "../BlurDetector.hh"
#include "../ImageEncoder.hh"

class LoadingPipeline :
	public Drain<std::string>,
	public Exhaust<ImageBlurEmbedding> {
private:

	Pipe<std::string> path_pipe{16};
	Pipe<Magick::Image> image_pipe{1};
	Pipe<ImageBlur> blur_pipe{1};
	Pipe<ImageBlurEmbedding> embedding_pipe{1};

	ImageLoader image_loader;
	BlurDetector blur_detector;
	ImageEncoder image_encoder;

public:
	explicit LoadingPipeline(
		const char *blur_model_path,
		const char *encoder_model_path,
		const std::function<bool(const std::string &path)> &filter
	);

	PipeResult flush(std::string &&item, bool block) override;

	PipeResult suck(ImageBlurEmbedding &item, bool block) override;

	void dry() override;

	void plug() override;
};

/*#ifdef __cplusplus
}
#endif*/
#endif //CORE_LOADING_PIPELINE_HH
