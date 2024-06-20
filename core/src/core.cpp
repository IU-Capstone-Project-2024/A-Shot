#include "core.h"

#include <iostream>
#include <onnxruntime_cxx_api.h>

void hello(const char *model_path) {
	std::cout << "Hello, World!" << std::endl;

	Ort::Env env = Ort::Env(ORT_LOGGING_LEVEL_VERBOSE);
	Ort::AllocatorWithDefaultOptions allocator;

	Ort::SessionOptions session_options;
	Ort::Session session(env, model_path, session_options);

	size_t input_count = session.GetInputCount();
	for (size_t i = 0; i < input_count; ++i) {
		auto name = session.GetInputNameAllocated(i, allocator);
		std::cout << name.get() << std::endl;
	}
}
