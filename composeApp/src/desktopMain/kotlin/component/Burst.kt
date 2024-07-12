package component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.zIndex
import style.Roboto
import kotlin.random.Random

@Composable
fun Burst(
	modifier: Modifier = Modifier,
	shots: List<ImageBitmap>,
	caption: String? = null,
	maxN: Int = 3,
) {
	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		BoxWithConstraints(modifier = modifier) {
			val maxWidth = 1024 * 0.5f
			val maxHeight = 1024 * 0.25f
			shots.take(maxN)
				.forEachIndexed { index, bitmap ->
					Shot(
						modifier = Modifier
							.scale(2.3f / 3)
							.size(maxWidth.dp, maxHeight.dp)
							.offset {
								IntOffset(
									x = (maxWidth.toInt() * ((index - 1)) / 30),
									y = -((maxHeight.toInt() * ((index - 1)) / 30))
								)
							}
							.zIndex(maxN - 1 - index.toFloat()),
						image = bitmap,
						contentDescription = null,
					)
				}

			if (caption != null) {
				Text(
					text = caption,
					style = MaterialTheme.typography.body1.copy(
						fontSize = 35.sp,
						fontFamily = Roboto,
						fontWeight = FontWeight.Medium,
						color = Color(0xFF21005D)
					),
					modifier = Modifier
						//.scale(2.3f / 3)
						.offset(y = maxHeight.dp * 0.8f)
				)
			}
		}
	}
}


@Preview
@Composable
fun BurstPreview() {
	val image = useResource("icons/icon.png") {
		loadImageBitmap(it)
	}

	val n = Random.nextInt(2, 5)
	Burst(
		shots = List(n) { image },
		caption = "IMG_0002",
	)
}