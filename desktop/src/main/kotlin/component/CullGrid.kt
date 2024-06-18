package component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
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
	when (images.size) {
		2 -> CullGrid2(modifier, images[0], images[1])
		4 -> CullGrid4(modifier, images)
		else -> {
			// TODO: error
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
			// One on the top, another on the bottom
			ImageColumn(
				modifier.fillMaxSize(),
				a = a,
				b = b,
			)
		} else {
			ImageRow(
				modifier.fillMaxSize(),
				a = a,
				b = b,
			)
		}
	}
}

@Composable
private fun ImageColumn(
	modifier: Modifier = Modifier,
	a: ImageBitmap,
	b: ImageBitmap,
) {
	Column(
		modifier = modifier,
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center,
	) {
		Image(
			modifier = Modifier.weight(1f, false),
			bitmap = a,
			contentDescription = null,
		)

		Image(
			modifier = Modifier.weight(1f, false),
			bitmap = b,
			contentDescription = null,
		)
	}
}

@Composable
private fun ImageRow(
	modifier: Modifier = Modifier,
	a: ImageBitmap,
	b: ImageBitmap,
) {
	Row(
		modifier = modifier,
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.Center,
	) {
		Image(
			modifier = Modifier.weight(1f, false),
			bitmap = a,
			contentDescription = null,
		)

		Image(
			modifier = Modifier.weight(1f, false),
			bitmap = b,
			contentDescription = null,
		)
	}
}

@Composable
fun CullGrid4(
	modifier: Modifier = Modifier,
	/*@Size(4)*/ images: List<ImageBitmap>,
) {
	BoxWithConstraints(modifier = modifier) {
		Column(
			modifier = Modifier.fillMaxSize(),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			ImageRow(
				modifier = Modifier.weight(1f, false),
				a = images[0],
				b = images[1],
			)

			ImageRow(
				modifier = Modifier.weight(1f, false),
				a = images[2],
				b = images[3],
			)
		}
	}
}

@Composable
@Preview
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
			images = images
		)
	}
}

@Composable
@Preview
fun CullGrid2Preview() {
	val images = List(2) { "stubs/putin.jpg" }
	CullGridPreview(images)
}

@Composable
@Preview
fun CullGrid4Preview() {
	val images = List(4) { "stubs/shaurma.jpg" }
	CullGridPreview(images)
}
