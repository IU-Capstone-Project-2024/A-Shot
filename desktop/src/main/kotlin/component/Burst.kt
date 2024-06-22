package component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.IntOffset
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
			val maxWidth = constraints.maxWidth
			val maxHeight = constraints.maxHeight

			// TODO:
			shots.take(maxN)
				.forEachIndexed { index, bitmap ->
					Shot(
						modifier = Modifier
							.fillMaxSize(0.6f)
							.offset {
								IntOffset(
									x = maxWidth * index / 10,
									y = maxHeight * index / 10,
								)
							},
						image = bitmap,
						contentDescription = null,
					)
				}
		}

		if (caption != null) {
			Text(text = caption)
		}
	}
}

@Composable
@Preview
private fun BurstPreview() {
	val image = useResource("icons/icon.png") {
		loadImageBitmap(it)
	}

	val n = Random.nextInt(2, 5)
	Burst(
		shots = List(n) { image },
		caption = "Hello",
	)
}
