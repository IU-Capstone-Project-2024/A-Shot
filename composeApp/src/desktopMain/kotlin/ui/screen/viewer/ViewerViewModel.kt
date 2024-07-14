package ui.screen.viewer

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ui.screen.virtual_folder.VirtualFolder

class ViewerViewModel(
	private val folders: List<VirtualFolder>,
	folderIndex: Int,
) : ViewModel() {
	private val _uiStateFlow = MutableStateFlow(ViewerUiState(folders, folderIndex, 0))
	val uiStateFlow = _uiStateFlow.asStateFlow()

	fun load(position: (current: Pair<Int, Int>) -> Pair<Int, Int> = { it }) {
		_uiStateFlow.update { state ->
			var (folderIndex, shotIndex) = position(Pair(state.folderIndex, state.shotIndex))
			folderIndex = when {
				folderIndex in folders.indices -> folderIndex
				folderIndex < 0 -> folders.indices.last
				else -> folders.indices.first
			}

			shotIndex = when {
				shotIndex in folders[folderIndex].shots.indices -> shotIndex

				shotIndex < 0 -> {
					folderIndex--
					if (folderIndex < 0) {
						folderIndex = folders.indices.last
					}
					folders[folderIndex].shots.indices.last
				}

				else -> {
					folderIndex++
					if (folderIndex > folders.indices.last) {
						folderIndex = folders.indices.first
					}
					folders[folderIndex].shots.indices.first
				}
			}

			ViewerUiState(folders, folderIndex, shotIndex)
		}
	}

	fun selectFolder(index: Int) = load { (_, _) -> Pair(index, 0) }
	fun nextFolder() = load { (folder, _) -> Pair(folder + 1, 0) }
	fun prevFolder() = load { (folder, _) -> Pair(folder - 1, 0) }

	fun selectShot(index: Int) = load { (folder, _) -> Pair(folder, index) }
	fun nextShot() = load { (folder, shot) -> Pair(folder, shot + 1) }
	fun prevShot() = load { (folder, shot) -> Pair(folder, shot - 1) }
}
