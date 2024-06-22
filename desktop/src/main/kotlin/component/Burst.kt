package component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.zIndex
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

		val pSizeX = 512
		val pSizeY = 256

		BoxWithConstraints(modifier = modifier) {
			val maxWidth = constraints.maxWidth
			val maxHeight = constraints.maxHeight

			shots.take(maxN)
				.forEachIndexed { index, bitmap ->
					Shot(
						modifier = Modifier
							.size(pSizeX.dp, pSizeY.dp)
							.offset {
								IntOffset(
									x = (maxWidth * (index - 1)) / 30,
									y = -((maxHeight * (index - 1)) / 40)
								)
							}
							.zIndex(maxN - 1 - index.toFloat()),
						image = bitmap,
						contentDescription = null,
					)
				}
		}

		if (caption != null) {
			Text(
				text = caption,
				color = Color(0xFF21005D),
				fontSize = 35.sp,
				fontWeight = FontWeight.Bold,
				modifier = Modifier
					.offset(y = (pSizeY * 2 / 30).dp)
			)
		}
	}
}


@Composable
@Preview
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
