#include <Magick++/Functions.h>
#include "core.hh"

int main(int argc, const char **argv) {
	Magick::InitializeMagick(argv[0]);
	hello();
}
