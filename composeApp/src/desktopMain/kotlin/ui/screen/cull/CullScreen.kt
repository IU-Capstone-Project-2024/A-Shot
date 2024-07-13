package ui.screen.cull

import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import logic.CullUiState
import logic.CullViewModel


@Composable
fun CullScreen(cullViewModel: CullViewModel) {
	val requester = remember { FocusRequester() }

	LaunchedEffect(Unit) {
		requester.requestFocus()
	}

	Box(
		modifier = Modifier
			.fillMaxSize()
			.focusRequester(requester)
			.focusable()
			.onKeyEvent { event ->
				if (event.type != KeyEventType.KeyUp) {
					return@onKeyEvent false
				}

				when (event.key) {
					Key.D, Key.Spacebar, Key.DirectionRight -> {
						cullViewModel.nextBatch()
					}

					Key.A, Key.DirectionLeft -> {
						cullViewModel.prevBatch()
					}

					else -> return@onKeyEvent false
				}
				true
			}
	) {
		val state by cullViewModel.state.collectAsState()

		val images by produceState(emptyList(), state.currentCluster, state.currentBatch) {
			val batch = state.batches[state.currentBatch]
			value = batch
				.map { id ->
					async(Dispatchers.IO) {
						cullViewModel.thumbnail(id)
					}
				}
				.awaitAll()
				.filterNotNull() // TODO:
		}

		CullGrid(
			modifier = Modifier.fillMaxSize(),
			images = images,
		)
	}
}
