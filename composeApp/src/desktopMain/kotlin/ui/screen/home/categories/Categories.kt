package ui.screen.home.categories

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun FolderScreen(
	favouriteCount: Int,
	regularCount: Int,
	blurryCount: Int,

	onStarredSelected: () -> Unit,
	onNormalSelected: () -> Unit,
	onBlurrySelected: () -> Unit,
) {
	CategoryGrid(
		modifier = Modifier.fillMaxSize(),

		favouriteCount = favouriteCount,
		regularCount = regularCount,
		blurryCount = blurryCount,

		onFavouriteSelected = onStarredSelected,
		onRegularSelected = onNormalSelected,
		onBlurrySelected = onBlurrySelected,
	)
}

@Preview
@Composable
fun FolderPreview() {
	FolderScreen(
		favouriteCount = 120,
		regularCount = 3141,
		blurryCount = 1,

		onStarredSelected = {},
		onNormalSelected = {},
		onBlurrySelected = {},
	)
}
