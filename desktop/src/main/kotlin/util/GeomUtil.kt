package util

import androidx.compose.ui.geometry.Size

fun inscribe(image: Size, viewport: Size): Size {
	val ratio = (viewport.width * image.height) / (viewport.height * image.width)
	return if (ratio > 1f) {
		Size(viewport.height * image.width / image.height, viewport.height)
	} else {
		Size(viewport.width, viewport.width * image.height / image.width)
	}
}

fun Size.area(): Float {
	return this.width * this.height
}

fun Size(width: Int, height: Int): Size {
	return Size(width.toFloat(), height.toFloat())
}
