package screen.cull.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import shot.ShotGroup

@Composable
fun GroupsList(
	modifier: Modifier = Modifier,
	// TODO: highlight current group
	current: Int,
	groups: List<ShotGroup>,
	onGroupSelected: (Int) -> Unit,
) {
	LazyColumn(
		modifier = modifier
	) {
		itemsIndexed(groups) { index, group ->
			GroupItem(
				index = index,
				group = group,
				onClick = { onGroupSelected(index) },
			)
		}
	}
}

// TODO: seems to be duplicate with SubgroupItem
@Composable
fun GroupItem(
	index: Int,
	group: ShotGroup,
	onClick: () -> Unit,
) {
	Box(
		modifier = Modifier.aspectRatio(1.0f)
			.clickable(onClick = onClick)
	) {
		// Display the folder image for each group
		val image by produceState<ImageBitmap?>(null) {
			// TODO: handle the result
			value = group.thumbnail().getOrNull()
		}
    val folderImage: ImageBitmap = useResource("folder.png") { loadImageBitmap(it) }
		val currentImage = image
		if (currentImage == null) {
			CircularProgressIndicator(
				modifier = Modifier.align(Alignment.Center),
			)
		} else {
			Image(
				modifier = Modifier.fillMaxSize(),
				bitmap = folderImage,
				alignment = Alignment.Center,
				contentScale = ContentScale.Crop,
				contentDescription = null,
			)
		}

		Text(
			text = "Group #$index",
			color = Color.Black,
			fontSize = 16.sp,
			textAlign = TextAlign.Center,
			modifier = Modifier
				.fillMaxWidth()
				.align(Alignment.BottomCenter)
		)
	}
}
