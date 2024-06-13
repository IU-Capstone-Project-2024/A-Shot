package page.oknok

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun OkNokPage(
	onOkClick: () -> Unit,
	onNokClick: () -> Unit,
) {
	Row(
		modifier = Modifier.fillMaxSize()
	) {
		Button(
			onClick = onOkClick,
		) {
			Text("Ok")
		}

		Button(
			onClick = onNokClick,
		) {
			Text("Nok")
		}
	}
}
