package util

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import java.awt.Dimension
import java.io.File

sealed class ImageState {
	data object Loading : ImageState()
	data object Failure : ImageState()
	data class Success(val bitmap: ImageBitmap) : ImageState()
}

@Composable
fun AsyncImage(
	modifier: Modifier = Modifier,
	file: File,
	dimension: Dimension = Dimension(300, 300),
	contentScale: ContentScale = ContentScale.Crop,
	alignment: Alignment = Alignment.Center,
	contentDescription: String? = null,
) {
	val bitmap by produceState<ImageState>(ImageState.Loading, file, dimension) {
		val result = ImageLoader.load(file, dimension)
		value = if (result == null) {
			ImageState.Failure
		} else {
			ImageState.Success(result.toComposeImageBitmap())
		}
	}

	Crossfade(
		modifier = modifier,
		targetState = bitmap,
	) { state ->
		Box(modifier = Modifier.fillMaxSize()) {
			when (state) {
				ImageState.Failure -> {

				}

				ImageState.Loading -> {
					// TODO:
					// CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
				}

				is ImageState.Success -> {
					Image(
						modifier = Modifier.fillMaxSize(),
						bitmap = state.bitmap,
						contentDescription = contentDescription,
						contentScale = contentScale,
						alignment = alignment,
					)
				}
			}
		}
	}
}
