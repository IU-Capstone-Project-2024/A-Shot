//
// Created by a on 6/21/24.
//

#ifndef CORE_PIPELINE_STEP_HH
#define CORE_PIPELINE_STEP_HH

#include <thread>
#include <iostream>
#include "Pipe.hh"

template<typename I, typename O>
class PipelineStep {
protected:
	std::thread worker;
	Exhaust<I> &input;
	Drain<O> &output;

	virtual void process(I &input) = 0;

	virtual void run() {
		for (I i; !input.suck(i, true);) {
			try {
				process(i);
			} catch (const std::exception &e) {
				std::cout << e.what() << std::endl;
			}
		}
		input.plug();
		output.dry();
	}

public:
	PipelineStep(
		Exhaust<I> &input,
		Drain<O> &output
	) :
		input(input),
		output(output),
		worker(&PipelineStep::run, this) {
	}

	virtual ~PipelineStep() {
		input.plug();
		output.dry();
		worker.join();
	}
};

#endif //CORE_PIPELINE_STEP_HH
