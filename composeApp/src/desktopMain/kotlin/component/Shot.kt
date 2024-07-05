package component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
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
import style.Roboto

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
				.padding(4.dp)
				.clip(RoundedCornerShape(8.dp))
				.border(BorderStroke(6.dp, Color(0xFFFFD8E4)), RoundedCornerShape(8.dp))
				.wrapContentSize()
		) {
			Image(
				modifier = Modifier.padding(4.dp),
				bitmap = image,
				contentDescription = contentDescription,
			)
		}

		if (caption != null) {
			Text(
				text = "IMG_0001",
				style = MaterialTheme.typography.body1.copy(
					fontSize = 40.sp,
					fontFamily = Roboto,
					fontWeight = FontWeight.Medium,
					color = Color(0xFF21005D)
				)
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
