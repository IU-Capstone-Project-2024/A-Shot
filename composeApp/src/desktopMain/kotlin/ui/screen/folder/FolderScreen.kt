package ui.screen.folder

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun FolderScreen(
	starredCount: Int,
	normalCount: Int,
	blurryCount: Int,

	onStarredSelected: () -> Unit,
	onNormalSelected: () -> Unit,
	onBlurrySelected: () -> Unit,
) {
	CategoryGrid(
		modifier = Modifier.fillMaxSize(),

		starredCount = starredCount,
		normalCount = normalCount,
		blurryCount = blurryCount,

		onStarredSelected = onStarredSelected,
		onNormalSelected = onNormalSelected,
		onBlurrySelected = onBlurrySelected,
	)
}

@Preview
@Composable
fun FolderPreview() {
	FolderScreen(
		starredCount = 120,
		normalCount = 3141,
		blurryCount = 1,

		onStarredSelected = {},
		onNormalSelected = {},
		onBlurrySelected = {},
	)
}
