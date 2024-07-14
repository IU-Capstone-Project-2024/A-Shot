package ui.screen.home.categories.regular

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import ui.screen.virtual_folder.VirtualFolderGrid
import ui.screen.virtual_folder.VirtualFolderUiState

@Composable
fun RegularScreen(
	state: VirtualFolderUiState,
	thumbnail: suspend (shotId: Long) -> ImageBitmap?,
	onVirtualFolderClicked: (Int) -> Unit,
) {
	Box(modifier = Modifier.fillMaxSize()) {
		when (state) {
			VirtualFolderUiState.Idle, is VirtualFolderUiState.Loading -> {

			}

			is VirtualFolderUiState.Success -> {
				VirtualFolderGrid(
					modifier = Modifier.fillMaxSize(),
					folders = state.folders,
					thumbnail = thumbnail,
					onFolderClicked = onVirtualFolderClicked,
				)
			}
		}
	}
}
