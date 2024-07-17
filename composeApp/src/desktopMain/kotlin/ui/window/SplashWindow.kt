package ui.window

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import core.LoadingPipeline
import database.ShotDatabase
import ui.screen.splash.SplashScreen

@Composable
fun StarterWindow(
	onCloseRequest: () -> Unit,
	onReady: (ShotDatabase, LoadingPipeline) -> Unit,
) {
	Window(
		onCloseRequest = onCloseRequest,
		state = rememberWindowState(
			placement = WindowPlacement.Floating,
			position = WindowPosition.Aligned(Alignment.Center),
		),
		title = "A-Shot",
		undecorated = true
	) {
		SplashScreen(onReady = onReady)
	}
}
