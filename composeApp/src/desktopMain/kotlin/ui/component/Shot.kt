package ui.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp

@Composable
fun Shot(
	modifier: Modifier = Modifier,
	image: ImageBitmap?
) {
	Box(modifier = modifier) {
		if (image == null) {
			// TODO:
		} else {
			Image(
				modifier = Modifier
					.wrapContentSize()
					.border(BorderStroke(3.dp, Color(0xFFFFD8E4)), RoundedCornerShape(8.dp))
					.clip(RoundedCornerShape(8.dp)),
				bitmap = image,
				contentDescription = null,
			)
		}
	}
}


@Preview
@Composable
fun ShotPreview() {
	val image = remember {
		useResource("icons/icon.png") {
			loadImageBitmap(it)
		}
	}

	Shot(
		modifier = Modifier.wrapContentSize(),
		image = image
	)
}
