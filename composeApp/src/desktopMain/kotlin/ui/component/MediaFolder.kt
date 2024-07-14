package ui.component

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawTransform
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.rememberTextMeasurer
import ui.component.MediaCardConstants.TRANSFORMATIONS
import ui.stubImageBitmap
import kotlin.math.min
import kotlin.random.Random

@Composable
fun MediaFolder(
	modifier: Modifier = Modifier,
	label: String? = null,
	caption: String? = null,
	thumbs: List<ImageBitmap>? = null,
) {
	val transition = updateTransition(
		targetState = thumbs.isNullOrEmpty(),
		label = "Folder"
	)
	val coverSkew by transition.animateFloat(
		label = "Cover skew",
		transitionSpec = {
			tween(
				durationMillis = 300,
				delayMillis = 250
			)
		}
	) { closed ->
		if (closed) 0f else 1f
	}
	val contentScale by transition.animateFloat(
		label = "Content scale",
		transitionSpec = {
			tween(
				durationMillis = 500,
				delayMillis = 250
			)
		}
	) { closed ->
		if (closed) 0f else 1f
	}

	val textMeasurer = rememberTextMeasurer()

	Canvas(
		modifier = modifier.aspectRatio(1f)
	) {
		drawFolder(
			textMeasurer = textMeasurer,
			label = label,
			caption = caption,
			coverSkew = coverSkew
		) {
			if (!thumbs.isNullOrEmpty()) {
				val transformations = TRANSFORMATIONS[min(thumbs.size, TRANSFORMATIONS.size) - 1]
				thumbs.zip(transformations).forEach { (thumb, transform) ->
					withTransform({
						scale(contentScale, Offset(size.width / 2, size.height))
						transform()
					}) {
						drawPhotoCard(
							image = thumb,
							alpha = contentScale,
							// color = cardColor
						)
					}
				}
			}
		}
	}
}

typealias Transformation = DrawTransform.() -> Unit

private object MediaCardConstants {
	val TRANSFORMATIONS: Array<Array<Transformation>> = arrayOf(
		arrayOf(
			{
				scale(0.65f)
				translate(-0.05f * size.width, 0f)
				rotate(-5f)
			}
		),
		arrayOf(
			{
				scale(0.6f)
				translate(-0.2f * size.width, -0.05f * size.height)
				rotate(-20f)
			},
			{
				scale(0.5f)
				translate(0.3f * size.width, +0.2f * size.height)
				rotate(20f)
			}
		),
		arrayOf(
			{
				scale(0.6f)
				translate(0.05f * size.width, -0.1f * size.height)
				rotate(5f)
			},
			{
				scale(0.5f)
				translate(-0.35f * size.width, 0.2f * size.height)
				rotate(-20f)
			},
			{
				scale(0.4f)
				translate(0.5f * size.width, 0.4f * size.height)
				rotate(20f)
			},
		)
	)
}

@Preview
@Composable
private fun MediaFolderPreview() {
	val random = Random(42)
	MediaFolder(
		label = "Label",
		caption = "caption",
		thumbs = listOf(
			stubImageBitmap(random),
			stubImageBitmap(random),
			stubImageBitmap(random),
		)
	)
}
