package component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource

@Composable
fun Shot(
	modifier: Modifier = Modifier,
	image: ImageBitmap,
	caption: String? = null,
	contentDescription: String? = null,
) {
	Column(
		modifier = modifier,
		// Check:
		// https://developer.android.com/reference/kotlin/androidx/compose/foundation/layout/Arrangement
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Image(
			modifier = Modifier.wrapContentSize(),
			bitmap = image,
			contentDescription = contentDescription,
		)

		if (caption != null) {
			Text(text = caption)
		}
	}
}

@Composable
@Preview
fun ShotPreview() {
	val image = useResource("icons/icon.png") {
		loadImageBitmap(it)
	}

	Shot(
		modifier = Modifier.wrapContentSize(),
		image = image,
		caption = "Hello World!",
	)
}
