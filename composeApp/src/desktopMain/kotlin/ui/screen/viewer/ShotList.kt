package ui.screen.viewer

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import ui.component.PhotoCard
import ui.stubImageBitmap

@Composable
fun ShotList(
	modifier: Modifier = Modifier,
	shots: List<Long>,
	thumbnail: suspend (Long) -> ImageBitmap?
) {
	LazyColumn(
		modifier = modifier
	) {
		items(
			items = shots,
			key = { shotId -> shotId }
		) { shotId ->
			val image by produceState<ImageBitmap?>(null, shotId) {
				value = thumbnail(shotId)
			}

			PhotoCard(
				modifier = Modifier.aspectRatio(1f),
				image = image,
			)
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
		shots = remember { List(10) { 1 } },
		thumbnail = {
			stubImageBitmap()
		}
	)
}
