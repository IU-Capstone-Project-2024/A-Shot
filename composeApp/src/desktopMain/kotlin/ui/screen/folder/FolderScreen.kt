package ui.screen.folder

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun FolderScreen(
	niceCount: Int,
	unsortedCount: Int,
	unluckyCount: Int,

	onNiceSelected: () -> Unit,
	onUnsortedSelected: () -> Unit,
	onUnluckySelected: () -> Unit,
) {
	CategoryGrid(
		modifier = Modifier.fillMaxSize(),

		niceCount = niceCount,
		unsortedCount = unsortedCount,
		unluckyCount = unluckyCount,

		onNiceSelected = onNiceSelected,
		onUnsortedSelected = onUnsortedSelected,
		onUnluckySelected = onUnluckySelected,
	)
}

@Preview
@Composable
fun FolderPreview() {
	FolderScreen(
		niceCount = 120,
		unsortedCount = 3141,
		unluckyCount = 1,

		onNiceSelected = {},
		onUnsortedSelected = {},
		onUnluckySelected = {},
	)
}
