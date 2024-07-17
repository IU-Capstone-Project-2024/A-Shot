package ui.screen.splash

import androidx.annotation.FloatRange

data class SplashUiState(
	@FloatRange(0.0, 1.0) val progress: Float,
	val message: String
)
