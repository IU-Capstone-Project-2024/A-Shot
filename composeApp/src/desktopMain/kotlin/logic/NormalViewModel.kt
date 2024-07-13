package logic

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import database.dao.ShotDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import util.DBSCAN
import util.ShotCluster

sealed class NormalUiState {
	data object Loading : NormalUiState()
	data class Success(
		val folderId: Long,
		val clusters: List<ShotCluster> = emptyList(),
		val currentCluster: Int = 0,
	) : NormalUiState()
}

class NormalViewModel(
	private val shotDao: ShotDao,
) : ViewModel() {
	private val _state = MutableStateFlow<NormalUiState>(NormalUiState.Loading)
	val state = _state.asStateFlow()

	fun load(folderId: Long) {
		/*if (folderId == (_state.value as? NormalUiState.Success)?.folderId) {
			return
		}*/

		_state.value = NormalUiState.Loading
		viewModelScope.launch(Dispatchers.IO) {
			val embeddings = shotDao.embeddings(folderId, 0.05f)
			val dbscan = DBSCAN(0.9, 2)
			val clusterIndices = dbscan.cluster(embeddings.map { it.embedding })

			var max = clusterIndices.max()
			val result = clusterIndices
				.map {
					if (it == -1) {
						max += 1
						max
					} else {
						it
					}
				}
				.zip(embeddings)
				.groupBy({ it.first }, { it.second.id })
				.map { ShotCluster(it.key.toLong(), it.value) }
				.sortedBy { it.id }

			_state.value = NormalUiState.Success(
				folderId = folderId,
				clusters = result,
				currentCluster = 0
			)
		}
	}

	fun setCurrentCluster(index: Int, relative: Boolean = false) {
		_state.update { state ->
			when (state) {
				NormalUiState.Loading -> NormalUiState.Loading
				is NormalUiState.Success -> {
					val clusterIndex = (
							if (relative)
								state.currentCluster + index
							else
								index
							).coerceIn(0 until state.clusters.size)

					NormalUiState.Success(
						folderId = state.folderId,
						clusters = state.clusters,
						currentCluster = clusterIndex,
					)
				}
			}
		}
	}

	fun nextCluster() = setCurrentCluster(+1, true)
	fun prevCluster() = setCurrentCluster(-1, true)

	suspend fun thumbnail(shotId: Long): ImageBitmap? {
		return shotDao.thumbnail(shotId)?.toComposeImageBitmap()
	}
}
