package ui.screen.normal

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import logic.NormalUiState
import logic.NormalViewModel

@Composable
fun NormalScreen(
	normalViewModel: NormalViewModel,
	onClusterClicked: (Int) -> Unit,
) {
	Box(modifier = Modifier.fillMaxSize()) {
		val state by normalViewModel.state.collectAsState()

		when (val currentState = state) {
			NormalUiState.Loading -> {

			}

			is NormalUiState.Success -> {
				ClusterGrid(
					modifier = Modifier.fillMaxSize(),
					clusters = currentState.clusters,
					thumbnail = normalViewModel::thumbnail,
					onClusterClicked = onClusterClicked,
				)
			}
		}
	}
}
