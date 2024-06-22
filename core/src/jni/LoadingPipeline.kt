package core.src.jni

import shot.Shot
import shot.ShotCollection
import shot.ShotGroup
import java.io.File
import java.util.LinkedList

class LoadingPipeline(
	private val onProgressChange: (Float) -> Unit,
	private val onFinished: (ShotCollection) -> Unit
) {
	companion object {
		init {
			Core.load()
		}
	}

	private external fun nNew(): Long
	private external fun nRelease(ptr: Long)

	private val ptr: Long = nNew()
	private val good: MutableList<Shot> = LinkedList()
	private val bad: MutableList<Shot> = LinkedList()


	private fun sink(path: String?, score: Float) {
		if (path == null) {
			onFinished(ShotCollection(listOf(ShotGroup(good)), bad))
			// TODO: release
			return
		}

		val shot = Shot(File(path))
		if (1 - score > 0.95f) {
			bad.add(shot)
		} else {
			good.add(shot)
		}
		onProgressChange(0.1f)
	}
}