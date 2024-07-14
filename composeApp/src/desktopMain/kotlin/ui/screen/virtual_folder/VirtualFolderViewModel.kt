package ui.screen.virtual_folder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import database.selection.ShotIdEmbedding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import util.DBSCAN

class VirtualFolderViewModel(
	private val embeddings: suspend (folderId: Long) -> List<ShotIdEmbedding>
) : ViewModel() {
	private val _uiStateFlow = MutableStateFlow<VirtualFolderUiState>(VirtualFolderUiState.Idle)
	val uiStateFlow = _uiStateFlow.asStateFlow()

	private var job: Job? = null

	fun load(folderId: Long) {
		val oldState = _uiStateFlow.value
		var cancel = when (oldState) {
			VirtualFolderUiState.Idle -> false
			is VirtualFolderUiState.Loading -> oldState.folderId == folderId
			is VirtualFolderUiState.Success -> oldState.folderId == folderId
		}
		if (cancel) {
			return
		}

		val newState = VirtualFolderUiState.Loading(folderId)
		cancel = !_uiStateFlow.compareAndSet(oldState, newState)
		if (cancel) {
			return
		}

		job?.cancel()
		job = viewModelScope.launch(Dispatchers.IO) {
			val embeddings = embeddings(folderId)
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
				.map { VirtualFolder(it.key.toLong(), it.value) }
				.sortedBy { it.id }

			val resultState = VirtualFolderUiState.Success(folderId, result)
			_uiStateFlow.compareAndSet(newState, resultState)
		}
	}
}
