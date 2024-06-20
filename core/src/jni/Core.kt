package core.src.jni

object Core {
	init {
//		val resource = Core.javaClass.getResourceAsStream("libs/libcore.so")
//		val path = Files.createTempFile("libcore", ".so")
//		Files.copy(resource, path, StandardCopyOption.REPLACE_EXISTING)

		System.load("/home/a/Projects/A-Shot/desktop/src/main/kotlin/core/cmake-build-debug/libcore.so")
	}

	fun test(): Int {
		return 42
	}

	external fun hello(image: String): Int
}