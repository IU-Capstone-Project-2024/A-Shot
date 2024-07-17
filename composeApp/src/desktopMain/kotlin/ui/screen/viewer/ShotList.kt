package ui.screen.viewer

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import ui.component.PhotoCard
import ui.component.SelectionBox
import ui.stubImageBitmap

@Composable
fun ShotList(
	modifier: Modifier = Modifier,
	shots: List<Long>,
	currentShot: Int,
	thumbnail: suspend (Long) -> ImageBitmap?,
	onItemClick: (Int) -> Unit
) {
	val state = rememberLazyListState()

	LaunchedEffect(currentShot) {
		state.animateScrollToItem(currentShot)
	}

	LazyColumn(
		modifier = modifier,
		state = state,
		contentPadding = PaddingValues(4.dp),
		verticalArrangement = Arrangement.spacedBy(4.dp)
	) {
		itemsIndexed(
			items = shots,
			key = { _, shotId -> shotId }
		) { index, shotId ->
			val image by produceState<ImageBitmap?>(null, shotId) {
				value = thumbnail(shotId)
			}

			SelectionBox(
				modifier = Modifier
					.aspectRatio(1f)
					.clickable(onClick = { onItemClick(index) }),
				selected = index == currentShot
			) { modifier ->
				PhotoCard(
					modifier = modifier,
					image = image,
				)
			}
		}
	}
}

@Preview
@Composable
fun ShotListPreview() {
	ShotList(
		modifier = Modifier
			.fillMaxHeight()
			.width(256.dp),
		currentShot = 0,
		shots = remember { List(10) { 1 } },
		onItemClick = {},
		thumbnail = {
			stubImageBitmap()
		},
	)
}
