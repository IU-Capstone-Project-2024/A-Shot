package ui.screen.viewer

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import ui.component.SelectionBox
import ui.component.VirtualFolder
import ui.screen.virtual_folder.VirtualFolder
import ui.stubImageBitmap
import ui.stubListOfVirtualFolders

@Composable
fun FolderList(
	modifier: Modifier = Modifier,
	folders: List<VirtualFolder>,
	currentFolder: Int,
	thumbnail: suspend (Long) -> ImageBitmap?,
	onItemClick: (Int) -> Unit,
) {
	val state = rememberLazyListState()

	LaunchedEffect(currentFolder) {
		state.animateScrollToItem(currentFolder)
	}

	LazyColumn(
		modifier = modifier,
		state = state,
		verticalArrangement = Arrangement.spacedBy(4.dp),
		contentPadding = PaddingValues(16.dp),
	) {
		itemsIndexed(
			items = folders,
			key = { _, folder -> folder.id }
		) { index, folder ->
			SelectionBox(
				modifier = Modifier.clickable(onClick = { onItemClick(index) }),
				selected = index == currentFolder,
			) { modifier ->
				VirtualFolder(
					modifier = modifier,
					folder = folder,
					thumbnail = thumbnail,
				)
			}
		}
	}
}

@Preview
@Composable
fun FolderListPreview() {
	FolderList(
		modifier = Modifier
			.fillMaxHeight()
			.width(256.dp),
		currentFolder = 0,
		folders = stubListOfVirtualFolders(),
		thumbnail = {
			stubImageBitmap()
		},
		onItemClick = {}
	)
}
