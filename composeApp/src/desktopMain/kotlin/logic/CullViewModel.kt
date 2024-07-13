package logic

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.lifecycle.ViewModel
import database.dao.ShotDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import util.ShotCluster

data class CullUiState(
	val batches: List<List<Long>>,
	val currentBatch: Int = 0,
	val currentCluster: Int = 0,
)

class CullViewModel(
	initialCluster: Int,
	private val clusters: List<ShotCluster>,
	private val shotDao: ShotDao
) : ViewModel() {

	private val _state: MutableStateFlow<CullUiState>
	val state: StateFlow<CullUiState>

	init {
		val initial = if (clusters.isEmpty()) {
			CullUiState(emptyList(), 0, 0)
		} else {
			CullUiState(clusters[initialCluster].shots.chunked(4), 0, 0)
		}

		_state = MutableStateFlow(initial)
		state = _state.asStateFlow()
	}

	private fun circleIndex(index: Int, min: Int, max: Int): Int {
		return if (index < min) {
			max
		} else if (index > max) {
			min
		} else {
			index
		}
	}

	private fun setCurrentBatch(delta: Int) {
		_state.update { state ->
			var batches = state.batches
			var currentCluster = state.currentCluster
			var currentBatch = state.currentBatch + delta

			if (currentBatch < 0) {
				currentCluster = circleIndex(currentCluster - 1, 0, clusters.size - 1)
				batches = clusters[currentCluster].shots.chunked(4)
				currentBatch = batches.size - 1
			} else if (currentBatch >= state.batches.size) {
				currentCluster = circleIndex(currentCluster + 1, 0, clusters.size - 1)
				batches = clusters[currentCluster].shots.chunked(4)
				currentBatch = 0
			}

			println("$currentCluster $currentBatch")

			CullUiState(
				batches = batches,
				currentBatch = currentBatch,
				currentCluster = currentCluster,
			)
		}
	}

	fun nextBatch() = setCurrentBatch(+1)
	fun prevBatch() = setCurrentBatch(-1)

	suspend fun thumbnail(shotId: Long): ImageBitmap? {
		return shotDao.thumbnail(shotId)?.toComposeImageBitmap()
	}
}
