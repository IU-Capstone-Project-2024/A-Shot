package screen.importing

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import shot.ShotCollection
import java.io.File

@Composable
fun ImportScreen(
	dir: File,
	onImported: (ShotCollection) -> Unit,
	onClose: () -> Unit,
) {
	val model = remember(dir) { ImportModel(dir) }
	val state by model.stateFlow.collectAsState()

	LaunchedEffect(model) {
		model.import(onImported)
	}

	Column(
		modifier = Modifier.fillMaxSize(),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		when (val currentState = state) {
			is ImportState.Failure -> {
				Text(text = currentState.reason)
			}

			is ImportState.Loading -> {
				Text(text = "Importing ${dir.name}")
				Spacer(modifier = Modifier.height(40.dp))
				LinearProgressIndicator(progress = currentState.progress)
			}
		}

		Spacer(modifier = Modifier.height(40.dp))
		Button(onClick = onClose) {
			Text(text = "Cancel")
		}
	}
}
