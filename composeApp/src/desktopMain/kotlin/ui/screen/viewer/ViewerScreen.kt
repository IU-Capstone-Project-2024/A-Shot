package ui.screen.viewer

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.dp
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

	Row(
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

		FolderList(
			modifier = Modifier
				.width(256.dp)
				.fillMaxHeight(),
			folders = state.folders,
			thumbnail = thumbnail,
		)

		image?.let {
			Viewer(
				modifier = Modifier
					.fillMaxHeight()
					.weight(1f),
				image = it
			)
		} ?: run {

		}

		ShotList(
			modifier = Modifier
				.width(256.dp)
				.fillMaxHeight(),
			shots = state.folders[state.folderIndex].shots,
			thumbnail = thumbnail,
		)
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
