package ui.screen.overview

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import database.entity.Folder
import database.selection.FolderWithCount
import ui.stubListOfFolderWithCount

@Composable
fun CollectionGrid(
	modifier: Modifier = Modifier,
	folders: List<FolderWithCount>,
	onFolderSelected: (Folder) -> Unit
) {
	val interactionSource = remember { MutableInteractionSource() }

	LazyVerticalGrid(
		modifier = modifier,
		columns = GridCells.Adaptive(224.dp),
		contentPadding = PaddingValues(8.dp),
//		verticalArrangement = Arrangement.spacedBy(8.dp),
//		horizontalArrangement = Arrangement.spacedBy(8.dp)
	) {
		items(
			items = folders,
			key = { item -> item.folder.id },
		) { folderWithCount ->
			FolderItem(
				modifier = Modifier
					.aspectRatio(1f)
					.clickable(
						interactionSource = interactionSource,
						indication = null
					) {
						onFolderSelected(folderWithCount.folder)
					},
				folderWithCount = folderWithCount
			)
		}
	}
}

@Preview
@Composable
fun CollectionGridPreview() {
	CollectionGrid(
		modifier = Modifier.fillMaxSize(),
		folders = stubListOfFolderWithCount(),
		onFolderSelected = {}
	)
}
