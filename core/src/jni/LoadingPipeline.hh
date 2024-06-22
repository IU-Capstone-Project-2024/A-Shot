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

class LoadingPipeline :
	public Drain<std::string>,
	public Exhaust<ImageBlur> {
private:
	Pipe<std::string> path_pipe{4};
	Pipe<Magick::Image> image_pipe{1};
	Pipe<ImageBlur> blur_pipe{1};

	ImageLoader image_loader{path_pipe, image_pipe};
	BlurDetector blur_detector{image_pipe, blur_pipe};

public:
	PipeResult flush(std::string &&item, bool block) override;

	PipeResult suck(ImageBlur &item, bool block) override;

	void dry() override;

	void plug() override;
};

/*#ifdef __cplusplus
}
#endif*/
#endif //CORE_LOADING_PIPELINE_HH
