package component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun Shot(
	modifier: Modifier = Modifier,
	image: ImageBitmap,
	caption: String? = null,
	contentDescription: String? = null,
) {
	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Box(
			modifier = Modifier
				.padding(4.dp) // Padding around the image
				.clip(RoundedCornerShape(8.dp))
				.border(BorderStroke(6.dp, Color(0xFFFFD8E4)), RoundedCornerShape(8.dp))
				.wrapContentSize()
		) {
			Image(
				modifier = Modifier
					.padding(4.dp), // Padding inside the border
				bitmap = image,
				contentDescription = contentDescription,
			)
		}

		if (caption != null) {
			Text(
				text = caption,
				color = Color(0xFF21005D),
				fontSize = 40.sp,
				fontWeight = FontWeight.Bold
			)
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
		caption = "IMG_0001",
	)
}
