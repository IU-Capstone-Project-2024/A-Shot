package ui.screen.viewer

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import ui.stubImageBitmap

@Composable
fun Viewer(
	modifier: Modifier = Modifier,
	image: ImageBitmap,
) {
	Image(
		modifier = modifier,
		bitmap = image,
		contentDescription = null,
	)
}

@Preview
@Composable
fun ViewerPreview() {
	Viewer(
		image = stubImageBitmap()
	)
}
