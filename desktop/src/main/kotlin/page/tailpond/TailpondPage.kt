package page.tailpond

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import shot.Shot

@Composable
fun TailpondPage(
	shots: List<Shot>,
	onShotClick: (Int) -> Unit,
) {
	LazyVerticalGrid(
		modifier = Modifier.fillMaxSize(),
		columns = GridCells.Adaptive(120.dp),
	) {
		itemsIndexed(shots) { index, shot ->
			TailpondItem(
				shot = shot,
				onClick = { onShotClick(index) },
			)
		}
	}
}

@Composable
fun TailpondItem(
	shot: Shot,
	onClick: () -> Unit
) {
	Card(
		modifier = Modifier.aspectRatio(1f)
			.clickable(onClick = onClick),
	) {
		var image by remember { mutableStateOf<ImageBitmap?>(null) }
		val scope = rememberCoroutineScope()

		LaunchedEffect(shot) {
			scope.launch { image = shot.thumbnail().getOrNull() }
			// TODO: handle the result
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
			text = "${shot.file.name}!"
		)
	}
}
