package screen.import_

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import shot.Shot
import shot.ShotCollection
import shot.ShotGroup
import java.io.File

class ImportModel(
	private val onSuccess: (File, ShotCollection) -> Unit
) {

	private val scope = CoroutineScope(Dispatchers.IO)
	private val _stateFlow = MutableStateFlow<ImportState>(ImportState.SelectDir)
	val stateFlow = _stateFlow.asStateFlow()

	private val good = mutableListOf<Shot>()
	private val bad = mutableListOf<Shot>()

	fun reset() {
		_stateFlow.value = ImportState.SelectDir
	}

	fun import(dir: File) = scope.launch(Dispatchers.IO) {
		val numFiles = dir.listFiles { file ->
			file.extension in listOf("jpg", "jpeg", "png", "gif")
		}?.size
		val step = 1.0f / (numFiles ?: 1)
		while((good.size+bad.size)!=numFiles){
			_stateFlow.update { ImportState.Loading(dir, step * good.size+bad.size,(good.size+bad.size),numFiles) }
		}

		if (numFiles == null || numFiles == 0) {
			_stateFlow.update { ImportState.Failure("No images found in the folder") }
		} else {
			val grouped = good.chunked(10).map { group -> ShotGroup(group) }
			val result = ShotCollection(
				grouped,
				bad,
			)
			onSuccess(dir, result)
		}
	}

	fun adding(file: Shot, toGood: Boolean) {
		if (toGood) {
			good.add(file)
		} else {
			bad.add(file)
		}
	}
}
