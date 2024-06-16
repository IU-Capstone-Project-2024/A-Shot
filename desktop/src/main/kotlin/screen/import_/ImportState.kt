package screen.import_

import androidx.annotation.FloatRange
import java.io.File

sealed class ImportState {
	data object SelectDir : ImportState()

	data class Loading(
		val dir: File,

		@FloatRange(from = 0.0, to = 1.0)
		val progress: Float = 0.0f
	) : ImportState()

	data class Failure(val reason: String) : ImportState()
}
