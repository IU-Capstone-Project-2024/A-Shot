#include <Magick++/Functions.h>
#include "core.hh"

int main(int argc, const char **argv) {
	if (argc != 2) {
		printf("Expected path to image or folder\n");
		return 1;
	}

	Magick::InitializeMagick(argv[0]);
	hello(std::string(argv[1]));
}
