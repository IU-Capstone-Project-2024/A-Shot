package ui.screen.cull

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import ui.stubImageBitmap

@Composable
fun CullItem(
	modifier: Modifier = Modifier,
	image: ImageBitmap,
) {
	Box(modifier = modifier) {
		Image(
			modifier = Modifier.fillMaxSize(),
			bitmap = image,
			contentDescription = null,
		)
	}
}

@Preview
@Composable
fun CullItemPreview() {
	CullItem(
		modifier = Modifier.fillMaxSize(),
		image = stubImageBitmap(),
	)
}
