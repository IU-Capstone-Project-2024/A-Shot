package core

import kotlin.io.path.Path
import kotlin.io.path.div
import kotlin.io.path.exists

object Core {
	fun load() {
		val path = System.getenv("LD_LIBRARY_PATH")
			?: throw RuntimeException("Please provide native library path via LD_LIBRARY_PATH env variable")

		var loaded = false
		for (prefix in path.split(':')) {
			val library = Path(prefix) / "libcore.so"
			if (library.exists()) {
				System.load(library.toString())
				loaded = true
				break
			}
		}

		if (!loaded) {
			throw RuntimeException("Could not find libcore.so")
		}
	}

	external fun hello(): Int
}
