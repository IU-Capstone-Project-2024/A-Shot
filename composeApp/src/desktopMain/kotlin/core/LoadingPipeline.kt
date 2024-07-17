package core

import androidx.annotation.Keep
import database.dao.FolderDao
import database.dao.ShotDao
import kotlinx.coroutines.*
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.name

class LoadingPipeline(
	blurModel: String,
	embeddingModel: String,

	private val folderDao: FolderDao,
	private val shotDao: ShotDao
) {
	private class Result(
		val filepath: String,
		val blurScore: Float,
		val embedding: ByteArray,
		val thumbnail: ByteArray,
	)

	private external fun nNew(blurModel: String, embeddingModel: String): Long
	private external fun nRelease(ptr: Long)

	private external fun nFlush(ptr: Long, path: String): Boolean
	private external fun nSuck(ptr: Long): Result?

	private val ptr: Long = nNew(blurModel, embeddingModel)
	private val scope = CoroutineScope(Dispatchers.IO + Job())

	@Keep
	private fun filter(filepath: String): Boolean {
		return runBlocking {
			val path = Path(filepath)
			val folderPath = path.parent.absolutePathString()
			val fileName = path.name

			val contains = shotDao.contains(folderPath, fileName)
			!contains
		}
	}

	fun load(path: String): Boolean {
		return nFlush(ptr, path)
	}

	init {
		scope.launch {
			// todo: fix
			while (true) {
				val result = nSuck(ptr) ?: break

				val path = Path(result.filepath)
				val folderPath = path.parent.absolutePathString()
				val fileName = path.name

				folderDao.insert(folderPath)
				val folderId = folderDao.select(folderPath) ?: TODO("Handle")
				shotDao.insert(folderId, fileName, result.blurScore, result.embedding, result.thumbnail)
			}
		}
	}
}
