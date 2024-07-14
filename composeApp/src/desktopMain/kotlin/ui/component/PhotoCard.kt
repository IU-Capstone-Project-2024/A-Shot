package ui.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import ui.stubImageBitmap
import kotlin.math.min

fun DrawScope.inscribeImage(image: ImageBitmap, alpha: Float = 1f) {
	val cropOffset: IntOffset
	val cropSize: IntSize

	if ((image.width * size.height) / (image.height * size.width) > 1f) {
		cropSize = IntSize(
			(image.height * size.width / size.height).toInt(),
			image.height
		)
		cropOffset = IntOffset((image.width - cropSize.width) / 2, 0)
	} else {
		cropSize = IntSize(
			image.width,
			(image.width * size.height / size.width).toInt()
		)
		cropOffset = IntOffset(0, (image.height - cropSize.height) / 2)
	}

	drawImage(
		image = image,
		dstSize = IntSize(size.width.toInt(), size.height.toInt()),
		srcOffset = cropOffset,
		srcSize = cropSize,
		alpha = alpha
	)
}

fun DrawScope.inscribeRoundedImage(
	image: ImageBitmap?,
	radius: Float = min(size.width, size.height) * 0.1f,
	alpha: Float = 1f
) {
	clipPath(
		path = Path().apply {
			addRoundRect(
				RoundRect(
					Rect(Offset.Zero, size),
					CornerRadius(radius)
				)
			)
		}
	) {
		if (image != null) {
			inscribeImage(image, alpha)
		}
	}
}

fun DrawScope.drawPhotoCard(
	image: ImageBitmap?,
	color: Color = Color(0x25000000), alpha: Float = 1f,
	radius: Float = min(size.width, size.height) * 0.1f
) {
	drawRoundRect(color = color, cornerRadius = CornerRadius(radius), alpha = alpha)
	inset(radius / 2) {
		inscribeRoundedImage(image, radius / 2, alpha)
	}
}

@Composable
fun PhotoCard(modifier: Modifier = Modifier, image: ImageBitmap?, color: Color = Color(0x25000000), alpha: Float = 1f) {
	Canvas(modifier = modifier.aspectRatio(1f)) {
		drawPhotoCard(image = image, color = color, alpha = alpha)
	}
}

@Preview
@Composable
private fun PhotoCardPreview() {
	PhotoCard(
		image = stubImageBitmap()
	)
}

@Preview
@Composable
private fun PhotoCardListPreview() {
	LazyVerticalGrid(modifier = Modifier.fillMaxSize(), columns = GridCells.Adaptive(128.dp)) {
		items(count = 100) {
			PhotoCardPreview()
		}
	}
}
