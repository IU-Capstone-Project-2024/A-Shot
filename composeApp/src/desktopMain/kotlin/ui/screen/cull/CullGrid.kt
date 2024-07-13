package ui.screen.cull

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import util.Size
import util.area
import util.inscribe
import kotlin.math.max

@Composable
fun CullGrid(
	modifier: Modifier = Modifier,
	images: List<ImageBitmap>,
) {
	val x = if (images.size == 3) images.take(2) else images.take(4) // TODO:
	when (x.size) {
		1 -> Image(
			modifier = Modifier.fillMaxSize(),
			bitmap = images[0],
			contentDescription = null,
		)

		2 -> CullGrid2(
			modifier = modifier,
			a = x[0],
			b = x[1],
		)

		4 -> CullGrid4(
			modifier = modifier,
			images = x
		)

		else -> {

		}
	}
}

@Composable
fun CullGrid2(
	modifier: Modifier = Modifier,
	a: ImageBitmap,
	b: ImageBitmap,
) {
	BoxWithConstraints(modifier = modifier) {
		val viewport = Size(
			constraints.maxWidth.toFloat(),
			constraints.maxHeight.toFloat()
		)

		val hArea by derivedStateOf {
			val image = Size((a.width + b.width), max(a.height, b.height))
			inscribe(image, viewport).area()
		}

		val vArea by derivedStateOf {
			val image = Size(max(a.width, b.width), (a.height + b.height))
			inscribe(image, viewport).area()
		}

		if (vArea > hArea) {
			ImageColumn(
				modifier.fillMaxHeight(),
				horizontalAlignment = Alignment.CenterHorizontally,
				a = a,
				b = b,
			)
		} else {
			ImageRow(
				modifier.fillMaxWidth(),
				verticalAlignment = Alignment.CenterVertically,
				a = a,
				b = b,
			)
		}
	}
}

@Composable
fun CullGrid4(
	modifier: Modifier = Modifier,
	images: List<ImageBitmap>,
) {
	BoxWithConstraints(modifier = modifier) {
		Column(
			modifier = Modifier.fillMaxSize(),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			ImageRow(
				modifier = Modifier
					.fillMaxWidth()
					.weight(1f),
				verticalAlignment = Alignment.Bottom,
				a = images[0],
				b = images[1],
			)

			ImageRow(
				modifier = Modifier
					.fillMaxWidth()
					.weight(1f),
				verticalAlignment = Alignment.Top,
				a = images[2],
				b = images[3],
			)
		}
	}
}

@Composable
private fun ImageColumn(
	modifier: Modifier = Modifier,
	horizontalAlignment: Alignment.Horizontal,
	a: ImageBitmap,
	b: ImageBitmap,
) {
	Column(
		modifier = modifier,
		horizontalAlignment = horizontalAlignment,
		verticalArrangement = Arrangement.Center,
	) {
		Image(
			modifier = Modifier.weight(1f).fillMaxSize(),
			alignment = Alignment.BottomCenter,
			bitmap = a,
			contentDescription = null
		)

		Image(
			modifier = Modifier.weight(1f).fillMaxSize(),
			alignment = Alignment.TopCenter,
			bitmap = b,
			contentDescription = null
		)
	}
}

@Composable
private fun ImageRow(
	modifier: Modifier = Modifier,
	verticalAlignment: Alignment.Vertical,
	a: ImageBitmap,
	b: ImageBitmap,
) {
	Row(
		modifier = modifier,
		verticalAlignment = verticalAlignment,
		horizontalArrangement = Arrangement.Center,
	) {
		Image(
			modifier = Modifier.weight(1f).fillMaxSize(),
			alignment = Alignment.CenterEnd,
			bitmap = a,
			contentDescription = null,
		)

		Image(
			modifier = Modifier.weight(1f).fillMaxSize(),
			alignment = Alignment.CenterStart,
			bitmap = b,
			contentDescription = null
		)
	}
}

@Preview
@Composable
fun CullGridPreview(paths: List<String>) {
	val images = paths.map { path ->
		useResource(path) { stream ->
			loadImageBitmap(stream)
		}
	}

	Box(modifier = Modifier.fillMaxSize()) {
		CullGrid(
			modifier = Modifier
				.fillMaxSize()
				.align(Alignment.Center),
			images = images,
		)
	}
}

@Preview
@Composable
fun CullGrid2Preview() {
	val images = List(2) { "stubs/putin.jpg" }
	CullGridPreview(images)
}

@Preview
@Composable
fun CullGrid4Preview() {
	val images = List(4) { "stubs/putin.jpg" }
	CullGridPreview(images)
}