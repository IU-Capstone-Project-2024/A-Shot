package component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.zIndex
import style.Roboto
import kotlin.math.max
import kotlin.math.min
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
		BoxWithConstraints(
			modifier = Modifier
				.aspectRatio(1f)
				.padding(8.dp)
		) {
			val containerSize = constraints.maxWidth
			val space = containerSize * 0.1f
			val contentSize = containerSize - space * max(0, min(shots.size, maxN) - 1)
			val bottomOffset = (containerSize - contentSize).toInt()

			shots.take(maxN)
				.forEachIndexed { index, bitmap ->
					Shot(
						modifier = Modifier
							.requiredSize(contentSize.dp)
							.offset {
								IntOffset(
									x = (space * index).toInt(),
									y = bottomOffset - (space * index).toInt(),
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
				textAlign = TextAlign.Center,
				text = caption,
				style = MaterialTheme.typography.body1.copy(
					fontSize = 35.sp,
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
fun BurstPreview() {
	val image = useResource("icons/icon.png") {
		loadImageBitmap(it)
	}

	val n = Random.nextInt(2, 5)
	Burst(
		modifier = Modifier.wrapContentSize(),
		shots = List(n) { image },
		caption = "IMG_0002",
	)
}