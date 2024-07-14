package ui.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import ui.screen.virtual_folder.VirtualFolder
import ui.stubImageBitmap
import ui.stubVirtualFolder

@Composable
fun VirtualFolder(
	modifier: Modifier = Modifier,
	folder: VirtualFolder,
	thumbnail: suspend (Long) -> ImageBitmap?,
) {
	if (folder.shots.size == 1) {
		val thumb by produceState<ImageBitmap?>(null, folder) {
			value = withContext(Dispatchers.IO) {
				val shotId = folder.shots.first()
				thumbnail(shotId)
			}
		}

		PhotoCard(
			modifier = modifier.aspectRatio(1f),
			image = thumb
		)
	} else {
		val thumbs by produceState<List<ImageBitmap>?>(null, folder) {
			value = folder.shots
				.take(3)
				.map {
					async {
						thumbnail(folder.id)
					}
				}
				.awaitAll()
				.filterNotNull()
		}

		MediaFolder(
			modifier = modifier.aspectRatio(1f),
			thumbs = thumbs,
			label = "${folder.id}",
			caption = "${folder.shots.size} items",
		)
	}
}

@Preview
@Composable
fun VirtualFolderPreview() {
	VirtualFolder(
		folder = stubVirtualFolder(),
		thumbnail = {
			stubImageBitmap()
		}
	)
}
