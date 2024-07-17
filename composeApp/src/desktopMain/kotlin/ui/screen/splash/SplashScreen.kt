package ui.screen.splash

import androidx.compose.runtime.*
import core.Core
import core.LoadingPipeline
import database.ShotDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(onReady: (ShotDatabase, LoadingPipeline) -> Unit) {
	var state by remember { mutableStateOf(SplashUiState(0.0f, "Starting up")) }

	LaunchedEffect(Unit) {
		launch(Dispatchers.IO) {
			delay(500)
			state = SplashUiState(0.0f, "Creating database")
			val db = ShotDatabase.instance

			state = SplashUiState(0.25f, "Loading blur detection neural network")
			val blurModel = Core.loadModel("ddffnet.ort")

			state = SplashUiState(0.5f, "Loading embedding neural network")
			val embeddingModel = Core.loadModel("cvnet50.ort")

			state = SplashUiState(0.75f, "Setting up the import pipeline")
			val loadingPipeline = LoadingPipeline(
				blurModel,
				embeddingModel,
				db.folderDao,
				db.shotDao
			)

			state = SplashUiState(1.0f, "Ready")
			delay(500)
			onReady(db, loadingPipeline)
		}
	}

	SplashContent(
		progress = state.progress,
		message = state.message,
	)
}
