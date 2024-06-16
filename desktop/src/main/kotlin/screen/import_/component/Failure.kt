package screen.import_.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun BoxScope.Failure(
	reason: String,
	onOk: () -> Unit
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.align(Alignment.Center),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Text(
			text = reason,
			maxLines = 2,
			overflow = TextOverflow.Ellipsis
		)
		Button(onClick = onOk) {
			Text("Ok")
		}
	}
}


@Composable
@Preview
private fun FailurePreview() {
	Container(Unit) {
		Failure(
			reason = "No images found in the folder",
			onOk = { }
		)
	}
}
