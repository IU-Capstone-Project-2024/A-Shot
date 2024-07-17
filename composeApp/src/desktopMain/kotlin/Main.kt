import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.application
import core.Core
import core.LoadingPipeline
import database.ShotDatabase
import ui.window.MainWindow
import ui.window.StarterWindow


sealed class State {
	data object Loading : State()
	data class Ready(val db: ShotDatabase, val loadingPipeline: LoadingPipeline) : State()
}

fun main() {
	Core.loadLibrary("libcore.so")

	application {
		var state by remember { mutableStateOf<State>(State.Loading) }

		when (val currentState = state) {
			State.Loading -> {
				StarterWindow(
					onCloseRequest = ::exitApplication,
					onReady = { db, loadingPipeline ->
						state = State.Ready(db, loadingPipeline)
					}
				)
			}

			is State.Ready -> {
				MainWindow(
					db = currentState.db,
					loadingPipeline = currentState.loadingPipeline,
					onCloseRequest = ::exitApplication,
				)
			}
		}
	}
}
