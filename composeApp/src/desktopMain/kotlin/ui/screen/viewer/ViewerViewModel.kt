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

	fun load(delta: Int) {
		_uiStateFlow.update { state ->
			var folderIndex = state.folderIndex
			var shotIndex = state.shotIndex + delta

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

	fun nextShot() {
 		load(+1)
	}

	fun prevShot() {
		load(-1)
	}
}
