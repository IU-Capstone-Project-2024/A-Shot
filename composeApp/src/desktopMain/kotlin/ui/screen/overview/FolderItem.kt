package ui.screen.overview

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import database.selection.FolderWithCount
import ui.component.Folder
import ui.stubFolderWithCount
import ui.stubFolderWithShots
import kotlin.io.path.Path
import kotlin.io.path.name

@Composable
fun FolderItem(
	modifier: Modifier = Modifier,
	folderWithCount: FolderWithCount,
) {
	val folder = folderWithCount.folder
	val count = folderWithCount.count
	val folderName by derivedStateOf { Path(folder.path).name }

	Folder(
		modifier = modifier,
		label = folderName,
		caption = "$count shots",
	)
}

@Preview
@Composable
fun FolderItemPreview() {
	FolderItem(
		modifier = Modifier.fillMaxWidth(),
		folderWithCount = stubFolderWithCount(),
	)
}