//
// Created by a on 6/21/24.
//

#ifndef CORE_LOADING_PIPELINE_HH
#define CORE_LOADING_PIPELINE_HH
/*#ifdef __cplusplus
extern "C" {
#endif*/

#include "Core.h"

class LoadingPipeline {
private:
	jobject j_this;

public:

	explicit LoadingPipeline(jobject j_this);
};

JNIEXPORT jlong JNICALL Java_core_src_jni_LoadingPipeline_nNew(
	JNIEnv *,
	jobject
);

/*#ifdef __cplusplus
}
#endif*/
#endif //CORE_LOADING_PIPELINE_HH
