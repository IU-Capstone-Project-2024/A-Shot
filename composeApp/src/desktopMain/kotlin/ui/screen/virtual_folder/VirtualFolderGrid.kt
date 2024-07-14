package ui.screen.virtual_folder

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import ui.component.VirtualFolder
import ui.stubListOfVirtualFolders

@Composable
fun VirtualFolderGrid(
	modifier: Modifier,
	folders: List<VirtualFolder>,
	thumbnail: suspend (Long) -> ImageBitmap?,
	onFolderClicked: (Int) -> Unit,
) {
	val interactionSource = remember { MutableInteractionSource() }

	LazyVerticalGrid(
		modifier = modifier,
		columns = GridCells.Adaptive(256.dp),
		contentPadding = PaddingValues(16.dp),
		verticalArrangement = Arrangement.spacedBy(8.dp),
		horizontalArrangement = Arrangement.spacedBy(8.dp)
	) {
		itemsIndexed(
			items = folders,
			key = { _, item -> item.id },
		) { index, folder ->
			VirtualFolder(
				modifier = Modifier.clickable(
					interactionSource = interactionSource,
					indication = null
				) {
					onFolderClicked(index)
				},
				folder = folder,
				thumbnail = thumbnail
			)
		}
	}
}

@Preview
@Composable
fun VirtualFoldersGridPreview() {
	val image = remember {
		useResource("icons/icon.png") {
			loadImageBitmap(it)
		}
	}

	VirtualFolderGrid(
		modifier = Modifier.fillMaxSize(),
		folders = stubListOfVirtualFolders(),
		thumbnail = {
			image
		},
		onFolderClicked = {}
	)
}
