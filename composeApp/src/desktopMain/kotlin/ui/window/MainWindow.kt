package ui.window

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import core.LoadingPipeline
import database.ShotDatabase
import ui.screen.main.MainScreen

@Composable
fun MainWindow(
	db: ShotDatabase,
	loadingPipeline: LoadingPipeline,
	onCloseRequest: () -> Unit
) {
	Window(
		onCloseRequest = onCloseRequest,
		state = rememberWindowState(
			placement = WindowPlacement.Maximized,
			position = WindowPosition.PlatformDefault,
		),
		title = "A-Shot",
		icon = painterResource("icons/icon.png"),
		undecorated = false
	) {
		MainScreen(db) { path ->
			loadingPipeline.load(path)
		}
	}
}
