package page.groups

import androidx.compose.foundation.Image
import shot.ShotGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable
fun GroupsPage(
	groups: List<ShotGroup>,
	onGroupClick: (Int) -> Unit,
) {
	LazyVerticalGrid(
		modifier = Modifier.fillMaxSize(),
		columns = GridCells.Adaptive(120.dp),
	) {
		itemsIndexed(groups) { index, group ->
			GroupedItem(
				group = group,
				onClick = { onGroupClick(index) },
			)
		}
	}
}

@Composable
fun GroupedItem(
	group: ShotGroup,
	onClick: () -> Unit,
) {
	Card(
		modifier = Modifier.aspectRatio(1f)
			.clickable(onClick = onClick),
	) {
		val image by produceState<ImageBitmap?>(null) {
			// TODO: handle the result
			value = group.thumbnail().getOrNull()
		}

		val currentImage = image
		if (currentImage == null) {
			CircularProgressIndicator()
		} else {
			Image(
				modifier = Modifier.fillMaxSize(),
				bitmap = currentImage,
				alignment = Alignment.Center,
				contentScale = ContentScale.Crop,
				contentDescription = null,
			)
		}
		Text(
			text = "${group.shots.size} items"
		)
	}
}
