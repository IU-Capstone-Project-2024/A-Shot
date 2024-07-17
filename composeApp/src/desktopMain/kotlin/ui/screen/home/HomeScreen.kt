package ui.screen.home

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import database.entity.Folder
import database.selection.FolderWithCount
import ui.stubListOfFolderWithCount


@Composable
fun HomeScreen(
	folders: List<FolderWithCount>,
	onFolderSelected: (Folder) -> Unit
) {
	FolderGrid(
		modifier = Modifier.fillMaxSize(),
		folders = folders,
		onFolderSelected = onFolderSelected
	)
}

@Preview
@Composable
fun HomeScreenPreview() {
	HomeScreen(
		folders = stubListOfFolderWithCount(),
		onFolderSelected = {}
	)
}
