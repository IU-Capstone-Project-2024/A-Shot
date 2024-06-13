package screen.importing

import androidx.annotation.FloatRange

sealed class ImportState {
	data class Loading(
		@FloatRange(from = 0.0, to = 1.0)
		val progress: Float = 0.0f,
	) : ImportState()

	data class Failure(
		val reason: String
	) : ImportState()
}
