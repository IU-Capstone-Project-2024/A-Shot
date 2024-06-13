package shot

import androidx.compose.ui.graphics.ImageBitmap

class ShotGroup(
	val shots: List<Shot>
) {
	suspend fun thumbnail(): Result<ImageBitmap> {
		return if (shots.isEmpty()) {
			Result.failure(Exception("No shots available"))
		} else {
			shots.random().thumbnail()
		}
	}
}
