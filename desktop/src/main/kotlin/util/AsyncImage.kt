package util

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.Deferred
import java.awt.Dimension
import java.awt.image.BufferedImage
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
	var bitmap by remember { mutableStateOf<ImageState>(ImageState.Loading) }
	var task: Deferred<BufferedImage?>? = null

	LaunchedEffect(file) {
		runCatching {
			task = ImageLoader.load(file, dimension)
			val result = task?.await()?.toComposeImageBitmap()

			bitmap = if (result == null) {
				ImageState.Failure
			} else {
				ImageState.Success(result)
			}
			bitmap
		}
	}

	DisposableEffect(file) {
		onDispose {
			task?.cancel()
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
