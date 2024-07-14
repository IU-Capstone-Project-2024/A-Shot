package ui.screen.viewer

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import ui.component.VirtualFolder
import ui.screen.virtual_folder.VirtualFolder
import ui.stubImageBitmap
import ui.stubListOfVirtualFolders

@Composable
fun FolderList(
	modifier: Modifier = Modifier,
	folders: List<VirtualFolder>,
	thumbnail: suspend (Long) -> ImageBitmap?
) {
	LazyColumn(
		modifier = modifier
	) {
		items(
			items = folders,
			key = { folder -> folder.id }
		) { folder ->
			VirtualFolder(
				folder = folder,
				thumbnail = thumbnail,
			)
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
		folders = stubListOfVirtualFolders(),
		thumbnail = {
			stubImageBitmap()
		}
	)
}
