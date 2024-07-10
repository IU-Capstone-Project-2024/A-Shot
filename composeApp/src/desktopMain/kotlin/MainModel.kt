import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import shot.ShotCollection
import java.io.File

class MainModel {
	private val _stateFlow = MutableStateFlow(MainState())
	var stateFlow = _stateFlow.asStateFlow()
		private set

	fun reset() {
		_stateFlow.update { MainState() }
	}

	fun imported(dir: File, collection: ShotCollection) {
		_stateFlow.update { state -> state.copy(dir = dir, shots = collection) }
	}

	fun cull(group: Int) {
		_stateFlow.update { state ->
			state.copy(currentGroup = group.coerceIn(0, state.shots.grouped.size - 1))
		}
	}
}
