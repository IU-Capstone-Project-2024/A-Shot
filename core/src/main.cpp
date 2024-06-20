#include <cstdio>
#include <cstdlib>
#include "core.h"

int main(int argc, char **argv) {
	if (argc != 2) {
		printf("Invalid usage, expected: <model_path>");
		return EXIT_FAILURE;
	}

	hello(argv[1]);
}
