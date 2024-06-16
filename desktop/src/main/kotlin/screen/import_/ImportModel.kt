package screen.import_

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import shot.Shot
import shot.ShotCollection
import shot.ShotGroup
import java.io.File
import kotlin.random.Random

class ImportModel(private val onSuccess: (File, ShotCollection) -> Unit) {

	private val scope = CoroutineScope(Dispatchers.IO)
	private val _stateFlow = MutableStateFlow<ImportState>(ImportState.SelectDir)
	val stateFlow = _stateFlow.asStateFlow()

	fun reset() {
		_stateFlow.value = ImportState.SelectDir
	}

	fun import(dir: File) =
		scope.launch(Dispatchers.IO) {
			val n = 4
			val step = 1.0f / n
			repeat(n) { i ->
				// TODO: remove, just imitation of work
				delay(100)
				_stateFlow.update { ImportState.Loading(dir, step * i) }
			}

			val files = dir.listFiles { file ->
				file.extension in listOf("jpg", "jpeg", "png", "gif")
			}

			if (files.isNullOrEmpty()) {
				_stateFlow.update { ImportState.Failure("No images found in the folder") }
			} else {
				val i = Random.nextInt(0, files.size)
				val shots = files.map { file -> Shot(file) }.toList()

				val tailpond = shots.subList(i, shots.size)
				val grouped = shots.subList(0, i)
					.chunked(10)
					.map { group -> ShotGroup(group) }

				val result = ShotCollection(
					grouped,
					tailpond,
				)
				onSuccess(dir, result)
			}
		}
}
