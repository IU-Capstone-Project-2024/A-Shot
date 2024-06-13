import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import shot.ShotCollection
import java.io.File

class MainModel {
	private val stateFlow_ = MutableStateFlow(MainState())
	var stateFlow = stateFlow_.asStateFlow()
		private set

	fun reset() {
		stateFlow_.update { MainState() }
	}

	fun selected(dir: File) {
		stateFlow_.update { state -> state.copy(dir = dir) }
	}

	fun overview(shots: ShotCollection) {
		stateFlow_.update { state ->
			state.copy(shots = shots)
		}
	}

	fun cull(group: Int) {
		stateFlow_.update { state ->
			state.copy(currentGroup = group.coerceIn(0, state.shots.grouped.size - 1))
		}
	}
}
