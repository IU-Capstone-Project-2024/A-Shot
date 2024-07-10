package core

import kotlinx.coroutines.flow.flow

class LoadingPipeline {
	data class ImageBlur(val path: String, val score: Float)

	private external fun nNew(): Long
	private external fun nRelease(ptr: Long)

	private external fun nFlush(ptr: Long, path: String): Boolean
	private external fun nSink(ptr: Long): ImageBlur?

	private val ptr: Long = nNew()

	fun load(path: String): Boolean {
		return nFlush(ptr, path)
	}

	suspend fun flow() = flow {
		while (true) {
			val result: ImageBlur = nSink(ptr) ?: break
			emit(result)
		}
	}
}
