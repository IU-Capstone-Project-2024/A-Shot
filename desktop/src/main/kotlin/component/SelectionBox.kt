package component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SelectionBox(
	modifier: Modifier = Modifier,
	selected: Boolean = false,
	width: Dp = 6.dp,
	color: Color = Color(0xFF6750A4),
	cornerRadius: Dp = 12.dp,
	contentPadding: Dp = 30.dp,
	content: @Composable BoxScope.() -> Unit,
) {
	Box(
		modifier = modifier.border(
				width = width, color = color, shape = RoundedCornerShape(cornerRadius)
			).padding(contentPadding)
	) {
		content()
	}
}

@Composable
@Preview
fun SelectionBoxPreview() {
	SelectionBox(
		modifier = Modifier.wrapContentSize(),
		selected = true,
	) {
		BurstPreview()
	}
}
