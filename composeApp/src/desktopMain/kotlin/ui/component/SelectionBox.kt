package ui.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ui.StubImage

@Composable
fun SelectionBox(
	modifier: Modifier = Modifier,
	selected: Boolean,
	width: Dp = 4.dp,
	padding: Dp = 4.dp,
	color: Color = Color.Red,
	content: @Composable (Modifier) -> Unit,
) {
	val m by derivedStateOf {
		if (selected) {
//			modifier.then(Modifier.border(width, color).padding(width + padding))
			modifier.border(width, color)
		} else {
//			modifier.then(Modifier.padding(width + padding))
			modifier
		}
	}
	content(m)
}

@Preview
@Composable
fun SelectionBoxPreview() {
	SelectionBox(
		modifier = Modifier
			.padding(16.dp)
			.wrapContentSize(),
		selected = true,
	) { modifier ->
		StubImage(modifier)
	}
}
