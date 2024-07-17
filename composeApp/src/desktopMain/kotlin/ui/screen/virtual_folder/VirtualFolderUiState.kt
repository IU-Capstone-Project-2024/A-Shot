package ui.screen.virtual_folder

sealed class VirtualFolderUiState {
	data object Idle : VirtualFolderUiState()
	data class Loading(val folderId: Long) : VirtualFolderUiState()
	data class Success(val folderId: Long, val folders: List<VirtualFolder>) : VirtualFolderUiState()
}