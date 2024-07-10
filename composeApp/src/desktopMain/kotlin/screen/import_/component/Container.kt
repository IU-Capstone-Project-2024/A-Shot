package screen.import_.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp


@Composable
fun <T> Container(
	state: T,
	content: @Composable BoxScope.(T) -> Unit
) {
	Box(
		modifier = Modifier
			// TODO: replace this cringe
			.requiredSizeIn(
				minWidth = 400.dp, minHeight = 200.dp,
				maxWidth = 800.dp, maxHeight = 400.dp,
			)
			.clip(RoundedCornerShape(36.dp)),
	) {
		content(state)
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
