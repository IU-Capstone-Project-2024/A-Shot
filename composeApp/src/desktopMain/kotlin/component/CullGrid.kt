package component

import ContentColor
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import util.Size
import util.area
import util.inscribe
import kotlin.math.max

@Composable
fun CullGrid(
	modifier: Modifier = Modifier,
	images: List<ImageBitmap>,
	onButtonClick: (Int) -> Unit
) {

	val selectedIndex = remember(images) { mutableStateOf(0) }

	when (images.size) {
		2 -> CullGrid2(modifier, images[0], images[1], selectedIndex.value, onButtonClick = { index ->
			selectedIndex.value = index
			onButtonClick(index)
		})
		4 -> CullGrid4(modifier, images, selectedIndex.value, onButtonClick = { index ->
			selectedIndex.value = index
			onButtonClick(index)
		})
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
	selectedIndex: Int,
	onButtonClick: (Int) -> Unit
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
				modifier.fillMaxSize(),
				a = a,
				b = b,
				imageLabel = 1,
				selectedIndex = selectedIndex,
				onButtonClick = onButtonClick
			)
		} else {
			ImageRow(
				modifier.fillMaxSize(),
				a = a,
				b = b,
				imageLabel = 1,
				selectedIndex = selectedIndex,
				onButtonClick = onButtonClick
			)
		}
	}
}

@Composable
fun CullGrid4(
	modifier: Modifier = Modifier,
	images: List<ImageBitmap>,
	selectedIndex: Int,
	onButtonClick: (Int) -> Unit
) {
	BoxWithConstraints(modifier = modifier) {
		Column(
			modifier = Modifier.fillMaxSize(),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			ImageRow(
				modifier = Modifier.weight(1f, false).padding(8.dp),
				a = images[0],
				b = images[1],
				imageLabel = 1,
				selectedIndex = selectedIndex,
				onButtonClick = onButtonClick
			)

			ImageRow(
				modifier = Modifier.weight(1f, false).padding(8.dp),
				a = images[2],
				b = images[3],
				imageLabel = 3,
				selectedIndex = selectedIndex,
				onButtonClick = onButtonClick
			)
		}
	}
}

@Composable
private fun ImageColumn(
	modifier: Modifier = Modifier,
	a: ImageBitmap,
	b: ImageBitmap,
	imageLabel: Int,
	selectedIndex: Int,
	onButtonClick: (Int) -> Unit
) {
	Column(
		modifier = modifier,
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center,
	) {
		ImageWithButton(
			modifier = Modifier.weight(1f),
			bitmap = a,
			buttonIndex = 0,
			imageLabel = imageLabel,
			selectedIndex = selectedIndex,
			onButtonClick = onButtonClick
		)

		ImageWithButton(
			modifier = Modifier.weight(1f),
			bitmap = b,
			buttonIndex = 1,
			imageLabel = imageLabel + 1,
			selectedIndex = selectedIndex,
			onButtonClick = onButtonClick
		)
	}
}

@Composable
private fun ImageRow(
	modifier: Modifier = Modifier,
	a: ImageBitmap,
	b: ImageBitmap,
	imageLabel: Int,
	selectedIndex: Int,
	onButtonClick: (Int) -> Unit
) {
	Row(
		modifier = modifier,
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.Center,
	) {
		ImageWithButton(
			modifier = Modifier.weight(1f).padding(8.dp),
			bitmap = a,
			buttonIndex = 0,
			imageLabel = imageLabel,
			selectedIndex = selectedIndex,
			onButtonClick = onButtonClick
		)

		ImageWithButton(
			modifier = Modifier.weight(1f).padding(8.dp),
			bitmap = b,
			buttonIndex = 1,
			imageLabel = imageLabel + 1,
			selectedIndex = selectedIndex,
			onButtonClick = onButtonClick
		)
	}
}


@Composable
fun ImageWithButton(
	modifier: Modifier = Modifier,
	bitmap: ImageBitmap,
	buttonIndex: Int,
	imageLabel: Int,
	selectedIndex: Int,
	onButtonClick: (Int) -> Unit
) {
	Box(modifier = modifier) {
		Image(
			modifier = Modifier.fillMaxSize(),
			bitmap = bitmap,
			contentDescription = null,
		)

		CircleButton(
			onClick = { onButtonClick(imageLabel - 1) },  // Update here to pass the imageLabel - 1
			label = imageLabel.toString(),
			isSelected = selectedIndex == (imageLabel - 1),
			modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)
		)
	}
}

@Composable
fun CircleButton(onClick: () -> Unit, label: String, isSelected: Boolean, modifier: Modifier = Modifier) {
	Button(
		onClick = onClick,
		shape = CircleShape,
		colors = if (isSelected) ButtonDefaults.buttonColors(backgroundColor = ContentColor) else ButtonDefaults.buttonColors(Color.White),
		modifier = modifier
			.size(40.dp)
			.border(2.dp, if (isSelected) Color.White else ContentColor, CircleShape)
	) {
		Text(text = label, fontSize = 12.sp, color = if (isSelected) Color.White else ContentColor)
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
			onButtonClick = { buttonIndex ->
				// Handle button click
			}
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