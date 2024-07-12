package ui.screen.folder

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ui.component.Folder

@Composable
fun CategoryGrid(
	modifier: Modifier = Modifier,

	starredCount: Int,
	normalCount: Int,
	blurryCount: Int,

	onStarredSelected: () -> Unit,
	onNormalSelected: () -> Unit,
	onBlurrySelected: () -> Unit,
) {
	val interactionSource = remember { MutableInteractionSource() }

	LazyVerticalGrid(
		modifier = modifier,
		columns = GridCells.Adaptive(224.dp),
		contentPadding = PaddingValues(8.dp),
//		verticalArrangement = Arrangement.spacedBy(8.dp),
//		horizontalArrangement = Arrangement.spacedBy(8.dp)
	) {
		item {
			Folder(
				modifier = Modifier
					.aspectRatio(1f)
					.clickable(
						interactionSource = interactionSource,
						indication = null,
						onClick = onStarredSelected
					),
				label = "Starred",
				caption = "$starredCount items",
				backColor = Color(0xFFA1D665),
				frontColor = Color(0xCCC3E88D),
			)
		}

		item {
			Folder(
				modifier = Modifier
					.aspectRatio(1f)
					.clickable(
						interactionSource = interactionSource,
						indication = null,
						onClick = onNormalSelected
					),
				label = "Normal",
				caption = "$normalCount items",
			)
		}

		item {
			Folder(
				modifier = Modifier
					.aspectRatio(1f)
					.clickable(
						interactionSource = interactionSource,
						indication = null,
						onClick = onBlurrySelected
					),
				label = "Blurry",
				caption = "$blurryCount items",
				backColor = Color(0xFFCC4022),
				frontColor = Color(0xCCFF5733),
			)
		}
	}
}

@Preview
@Composable
fun CategoryGridContent() {
	CategoryGrid(
		modifier = Modifier.fillMaxSize(),

		starredCount = 1,
		normalCount = 2,
		blurryCount = 20,

		onStarredSelected = {},
		onNormalSelected = {},
		onBlurrySelected = {},
	)
}
