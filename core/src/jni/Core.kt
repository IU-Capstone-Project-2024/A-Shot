package core.src.jni

object Core {
	fun load() {
		System.load("/home/a/Projects/A-Shot/desktop/src/main/kotlin/core/cmake-build-debug/libcore.so")
	}

	external fun hello(image: String): Int
}