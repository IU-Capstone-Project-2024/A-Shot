//
// Created by a on 6/21/24.
//

#ifndef CORE_PIPELINE_STEP_HH
#define CORE_PIPELINE_STEP_HH

#include "Pipe.hh"

template<typename I, typename O>
class PipelineStep {
protected:
	Pipe<I> &input;
	Pipe<O> &output;

public:
	PipelineStep(
		Pipe<I> &input,
		Pipe<O> &output
	) : input(input),
		output(output) {
	};
};

#endif //CORE_PIPELINE_STEP_HH
