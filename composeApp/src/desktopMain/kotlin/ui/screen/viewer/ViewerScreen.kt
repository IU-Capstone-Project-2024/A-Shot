package ui.screen.viewer

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.key.*
import ui.stubImageBitmap
import ui.stubListOfVirtualFolders
import kotlin.coroutines.cancellation.CancellationException

@Composable
fun ViewerScreen(
	state: ViewerUiState,
	thumbnail: suspend (Long) -> ImageBitmap?,
	onNextShot: () -> Unit,
	onPrevShot: () -> Unit
) {
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
					Key.D, Key.Spacebar, Key.DirectionRight -> onNextShot()
					Key.A, Key.DirectionLeft -> onPrevShot()
					else -> return@onKeyEvent false
				}
				true
			}
	) {
		val image by produceState<ImageBitmap?>(null, state) {
			val folderIndex = state.folderIndex
			val shotIndex = state.shotIndex

			try {
				val shotId = state.folders[folderIndex].shots[shotIndex]
				value = thumbnail(shotId)
			} catch (e: CancellationException) {
				// TODO: ?)
			} catch (t: Throwable) {
				value = null
			}
		}

		image?.let {
			Viewer(
				modifier = Modifier.fillMaxSize(),
				image = it
			)
		} ?: run {

		}
	}
}

@Preview
@Composable
fun ViewerScreenPreview() {
	ViewerScreen(
		state = ViewerUiState(
			stubListOfVirtualFolders(),
			0,
			0
		),
		onNextShot = {},
		onPrevShot = {},
		thumbnail = { _ -> stubImageBitmap() }
	)
}
