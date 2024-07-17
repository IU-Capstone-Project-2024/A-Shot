package ui.screen.viewer

import ui.screen.virtual_folder.VirtualFolder

data class ViewerUiState(
	val folders: List<VirtualFolder>,
	val folderIndex: Int,
	val shotIndex: Int
)
