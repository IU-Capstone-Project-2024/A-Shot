package screen.cull.components

import shot.ShotGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import javax.imageio.ImageIO

@Composable
fun SubgroupsList(
	modifier: Modifier = Modifier,
	// TODO: highlight current group
	current: Int,
	subgroups: List<ShotGroup>,
	onSubgroupSelected: (Int) -> Unit,
) {
	LazyRow(modifier = modifier) {
		itemsIndexed(subgroups) { index, subgroup ->
			// Display subgroups
			SubgroupItem(
				subgroup = subgroup,
				onClick = { onSubgroupSelected(index) }
			)
		}
	}
}

@Composable
fun SubgroupItem(
	subgroup: ShotGroup,
	onClick: () -> Unit,
) {
	Box(
		modifier = Modifier.aspectRatio(1.0f)
			.clickable(onClick = onClick),
	) {
		// Display image for each subgroup
		val image by produceState<ImageBitmap?>(null) {
			// TODO: handle the result
			value = subgroup.thumbnail().getOrNull()
		}

		val currentImage = image
		if (currentImage == null) {
			CircularProgressIndicator(
				modifier = Modifier.align(Alignment.Center),
			)
		} else {
			Image(
				modifier = Modifier.fillMaxSize(),
				bitmap = currentImage,
				alignment = Alignment.Center,
				contentScale = ContentScale.Crop,
				contentDescription = null,
			)
		}
	}
}
