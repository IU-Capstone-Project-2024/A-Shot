import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.io.path.pathString

object Native {
	init {
		val resource = Native.javaClass.getResourceAsStream("libs/libnative.so")!!
		val path = Files.createTempFile("libnative", ".so")
		Files.copy(resource, path, StandardCopyOption.REPLACE_EXISTING)

		System.load(path.pathString)
	}

	fun test(): Int {
		return 42
	}

	external fun hello(): Int
}