package screen.import_.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

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
	Container(
		state = Unit,
		importButtonWidth = 1000.dp,
		importButtonHeight = 430.dp
	) {
		Box(
			modifier = Modifier.fillMaxSize(),
			contentAlignment = Alignment.Center)
		{
			// todo
		}
		Failure(
			reason = "No images found in the folder",
			onOk = { }
		)
	}
}
