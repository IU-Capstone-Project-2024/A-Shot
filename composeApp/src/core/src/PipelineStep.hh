//
// Created by a on 6/21/24.
//

#ifndef CORE_PIPELINE_STEP_HH
#define CORE_PIPELINE_STEP_HH

#include "Pipe.hh"

template<typename I, typename O>
class PipelineStep {
protected:
	Exhaust<I> &input;
	Drain<O> &output;

public:
	PipelineStep(
		Exhaust<I> &input,
		Drain<O> &output
	) : input(input),
		output(output) {
	};
};

#endif //CORE_PIPELINE_STEP_HH
