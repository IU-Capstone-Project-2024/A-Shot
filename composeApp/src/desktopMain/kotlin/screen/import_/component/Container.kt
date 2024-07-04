package screen.import_.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun <T> Container(
	state: T,
	importButtonWidth: Dp? = 1000.dp,
	importButtonHeight: Dp? = 430.dp,
	importButtonColor: Color = Color(0xFFEADDFF),
	content: @Composable BoxScope.(T) -> Unit
) {
	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(Color(0xFFFEF7FF)),
	) {
		Card(
			modifier = Modifier
				.let {
					if (importButtonWidth != null && importButtonHeight != null) {
						it.size(importButtonWidth, importButtonHeight)
					} else {
						it.fillMaxSize()
					}
				}
				.align(Alignment.Center),
			shape = RoundedCornerShape(40.dp),
			backgroundColor = importButtonColor
		) {
			Box(modifier = Modifier.fillMaxSize()) {
				content(state)
			}
		}
	}
}

@Composable
@Preview
fun ContainerPreview() {
	Container(
		state = Unit
	) {

	}
}
