package ui.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextOverflow

object FolderCardDefaults {
	val backColor = Color(0xFFFFA000)
	val frontColor = Color(0xCCFFC624)
	val labelColor = Color(0xCCFFFFFF)
}

private object FolderCardConstants {
	const val TOP_PADDING = 0.35f
	const val LEFT_PADDING = 0.05f
	const val RIGHT_PADDING = 0.15f
	const val CORNER_RADIUS = 0.1f
}

@Composable
fun Folder(
	modifier: Modifier = Modifier,
	label: String? = null,
	caption: String? = null,
	backColor: Color = FolderCardDefaults.backColor,
	frontColor: Color = FolderCardDefaults.frontColor,
	labelColor: Color = FolderCardDefaults.labelColor,
	coverSkew: Float = 0.75f,
	content: @Composable (BoxScope.() -> Unit) = {}
) {
	val textMeasurer = rememberTextMeasurer()
	Box(modifier = modifier
		.aspectRatio(1f)
		.drawWithCache {
			onDrawWithContent {
				drawFolder(
					textMeasurer = textMeasurer,
					label = label,
					caption = caption,
					backColor = backColor,
					frontColor = frontColor,
					labelColor = labelColor,
					coverSkew = coverSkew,
				) {
					this@onDrawWithContent.drawContent()
				}
			}
		}
	) {
		content()
	}
}

fun DrawScope.drawFolder(
	textMeasurer: TextMeasurer,
	label: String? = null,
	caption: String? = null,
	backColor: Color = FolderCardDefaults.backColor,
	frontColor: Color = FolderCardDefaults.frontColor,
	labelColor: Color = FolderCardDefaults.labelColor,
	coverSkew: Float,
	content: DrawScope.() -> Unit
) {
	val radius = size.minDimension * FolderCardConstants.CORNER_RADIUS
	val right = size.width * FolderCardConstants.RIGHT_PADDING
	val left = size.width * FolderCardConstants.LEFT_PADDING
	val top = size.height * FolderCardConstants.TOP_PADDING

	inset(left, top, right, 0f) {
		drawBack(radius, backColor)
	}

	content()

	inset(left, top + radius, right, 0f) {
		drawFront(radius, frontColor, right - left, coverSkew) {
			val labelRadius = radius / 2f
			inset(labelRadius, labelRadius, labelRadius, labelRadius) {
				drawLabel(textMeasurer, label, caption, labelRadius, labelColor)
			}
		}
	}
}

private fun DrawScope.drawBack(radius: Float, backColor: Color) {
	drawPath(
		path = Path().apply {
			val radiusB = radius / 3f
			val radiusT = radius - radiusB

			val lt = Offset(radius, radius)
			val lb = Offset(radius, size.height - radius)

			val rb = Offset(size.width - radius, size.height - radius)
			val rt = Offset(size.width - radius, 2 * radius)

			val mb = Offset(lt.x + size.width / 2, rt.y - radius - radiusB)
			val mt = Offset(mb.x - radiusB - radiusT, mb.y)

			arcTo(Rect(lt, radius), -90f, -90f, true)
			arcTo(Rect(lb, radius), 180f, -90f, false)
			arcTo(Rect(rb, radius), 90f, -90f, false)
			arcTo(Rect(rt, radius), 0f, -90f, false)
			arcTo(Rect(mb, radiusB), 90f, 90f, false)
			arcTo(Rect(mt, radiusT), 0f, -90f, false)
		},
		color = backColor
	)
}

private fun DrawScope.drawFront(
	radius: Float,
	frontColor: Color,
	rightSpace: Float,
	skew: Float,
	content: DrawScope.() -> Unit
) {
	drawIntoCanvas {
		with(it) {
			withTransform(
				{
					val scale = 1 - 0.4f * skew
					val angle = (rightSpace * skew) / (size.width * scale)

					val matrix = Matrix()
					matrix.translate(0f, size.height * (1 - scale))
					matrix.scale(1f, scale)
					transform(matrix)

					skew(-angle, 0f)
					translate(size.height * angle, 0f)
				}
			) {
				drawRoundRect(
					color = frontColor,
					cornerRadius = CornerRadius(radius, radius)
				)
				content()
			}
		}
	}
}

private fun DrawScope.drawLabel(
	textMeasurer: TextMeasurer,
	label: String?,
	caption: String?,
	radius: Float,
	labelColor: Color
) {
	inset(0.0f, 0.0f, size.width * 0.1f, size.height * 0.45f) {
		val r = radius / 2f
		val firstLineHeight = size.height * 0.6f
		val secondLineWidth = size.width * 0.5f
		val secondLineHeight = size.height - firstLineHeight

		drawPath(
			path = Path().apply {
				val lt = Rect(Offset(radius, radius), radius)
				val rt = Rect(Offset(size.width - radius, radius), radius)
				val rm = Rect(Offset(size.width - radius, firstLineHeight - radius), radius)
				val mm = Rect(Offset(secondLineWidth + r, firstLineHeight + r), r)
				val mb = Rect(Offset(secondLineWidth - r, size.height - r), r)
				val lb = Rect(Offset(radius, size.height - radius), radius)

				arcTo(lt, -180f, 90f, false)
				arcTo(rt, -90f, 90f, false)
				arcTo(rm, 0f, 90f, false)
				arcTo(mm, -90f, -90f, false)
				arcTo(mb, 0f, 90f, false)
				arcTo(lb, 90f, 90f, false)
			},
			color = labelColor
		)

		if (label != null) {
			inset(r, r, r, secondLineHeight + r / 2) {
				drawText(
					text = label,
					style = TextStyle(
						fontSize = (size.height * 0.8f).toSp(),
						color = Color.Black.copy(alpha = 0.8f) // todo: solve
					),
					softWrap = false,
					overflow = TextOverflow.Ellipsis,
					size = Size(size.width, size.height),
					textMeasurer = textMeasurer,
					maxLines = 1,
				)
			}
		}

		if (caption != null) {
			inset(r, firstLineHeight, secondLineWidth + r, r) {
				drawText(
					text = caption,
					style = TextStyle(
						fontSize = (size.height * 0.8f).toSp(),
						color = Color.Black.copy(alpha = 0.8f) // todo: solve
					),
					softWrap = false,
					overflow = TextOverflow.Ellipsis,
					size = Size(size.width, size.height),
					textMeasurer = textMeasurer,
					maxLines = 1,
				)
			}
		}
	}
}

@Preview
@Composable
private fun FolderPreview() {
	Folder(
		label = "Label, long text",
		caption = "Caption, 1Long",
		coverSkew = 1f
	) {
		Image(
			modifier = Modifier.fillMaxSize(),
			painter = painterResource("icons/icon.png"),
			contentDescription = null
		)
	}
}
