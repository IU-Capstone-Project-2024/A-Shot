package ui.component

import androidx.compose.animation.Crossfade
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ui.screen.virtual_folder.VirtualFolder
import ui.stubImageBitmap

@Composable
fun VirtualFolder(
	modifier: Modifier = Modifier,
	image: ImageBitmap?,
	count: Int
) {
	Box(modifier = modifier.aspectRatio(1f)) {
		PhotoCard(
			modifier = modifier.fillMaxSize(),
			image = image
		)

		if (count > 1) {
			Box(
				modifier = Modifier
					.padding(8.dp)
					.size(42.dp)
					.background(Color.White, CircleShape)
					.border(3.dp, Color.Black, CircleShape)
					.align(Alignment.TopEnd),
				contentAlignment = Alignment.Center,
			) {
				Text(
					modifier = Modifier
						.fillMaxWidth()
						.wrapContentHeight(),
					text = count.toString(),
					maxLines = 1,
					overflow = TextOverflow.Ellipsis,
					textAlign = TextAlign.Center,
					fontWeight = FontWeight.Bold,
					fontSize = 14.sp,
				)
			}
		}
	}
}

@Composable
fun VirtualFolder(
	modifier: Modifier = Modifier,
	folder: VirtualFolder,
	thumbnail: suspend (Long) -> ImageBitmap?,
) {
	val thumb by produceState<ImageBitmap?>(null, folder) {
		value = withContext(Dispatchers.IO) {
			val shotId = folder.shots.first()
			thumbnail(shotId)
		}
	}

	VirtualFolder(
		modifier = modifier,
		image = thumb,
		count = folder.shots.size,
	)
}

@Preview
@Composable
fun VirtualFolderPreview() {
	VirtualFolder(
		image = stubImageBitmap(),
		count = 11,
	)
}
