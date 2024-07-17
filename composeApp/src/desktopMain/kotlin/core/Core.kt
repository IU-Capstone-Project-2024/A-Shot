package core

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption

object Core {
	private val appDir: File

	init {
		val home = System.getProperty("user.home")
		appDir = File(home, ".ashot").also {
			it.mkdirs()
		}
	}

	fun loadLibrary(libName: String) {
		val lib = File(appDir, libName)
		val arch = System.getProperty("os.arch")
		var os = System.getProperty("os.name").lowercase()

		os = when {
			os.contains("win") -> "windows"
			os.contains("linux") -> "linux"
			os.contains("mac") -> "macos"
			else -> throw IllegalStateException("Unsupported OS: $os")
		}

		val libResource = "/lib/$arch/$os/$libName"
		val libStream = javaClass.getResourceAsStream(libResource)
			?: throw IllegalStateException("Unable to load native library")

		libStream.use {
			Files.copy(it, lib.toPath(), StandardCopyOption.REPLACE_EXISTING)
		}

		System.load(lib.canonicalPath)
	}

	suspend fun loadModel(modelName: String): String = withContext(Dispatchers.IO) {
		val model = File(appDir, modelName)

		if (!model.exists()) {
			val modelResource = "/model/$modelName"
			val modelStream = javaClass.getResourceAsStream(modelResource)
				?: throw IllegalStateException("Unable to find the model")

			modelStream.use {
				Files.copy(it, model.toPath(), StandardCopyOption.REPLACE_EXISTING)
			}
		}

		model.canonicalPath
	}

	fun dbPath(): String {
		return File(appDir, "db").canonicalPath
	}

	external fun hello(): Int
}
