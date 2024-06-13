package screen.main

import PhotoGroup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.random.Random

class MainViewModel(private val dir: File) {

	private val stateFlow_ = MutableStateFlow<MainState>(MainState.Loading)
	var stateFlow = stateFlow_.asStateFlow()
		private set

	suspend fun loadImages() = withContext(Dispatchers.IO) {
		val files = dir.listFiles { file -> file.extension in listOf("jpg", "jpeg", "png", "gif") }
		val state = if (files.isNullOrEmpty()) {
			MainState.Failure
		} else {
			val n = Random.nextInt(2, 10)
			val groups = files.withIndex()
				.groupBy { (index, _) -> index % n }.values
				.map { f ->
					PhotoGroup(f.map { it.value }.toTypedArray())
				}
				.toTypedArray()
			MainState.Success(groups, emptyArray())
		}
		stateFlow_.update { state }
	}
}