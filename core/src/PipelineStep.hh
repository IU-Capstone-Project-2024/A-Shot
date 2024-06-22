//
// Created by a on 6/21/24.
//

#ifndef CORE_PIPELINE_STEP_HH
#define CORE_PIPELINE_STEP_HH

#include "Pipe.hh"

template<typename I, typename O>
class PipelineStep {
protected:
	SupplyPipe<I> &input;
	DrainPipe<O> &output;

public:
	PipelineStep(
		SupplyPipe<I> &input,
		DrainPipe<O> &output
	) : input(input),
		output(output) {
	};
};

#endif //CORE_PIPELINE_STEP_HH
