package screen.import_.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color


@Composable
fun <T> Container(
	state: T,
	content: @Composable BoxScope.(T) -> Unit
) {
	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(Color.LightGray),
	) {
		Card(
			// TODO: determine the size in a normal way
			modifier = Modifier
				.fillMaxSize(0.3f)
				.align(Alignment.Center),
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
	Container(Unit) {

	}
}
