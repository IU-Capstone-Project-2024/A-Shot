package screen.cull

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.ImageBitmap
import shot.ShotGroup
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import shot.Shot
import javax.imageio.ImageIO

class CullGroup (
   val group: ShotGroup,
   val subgroups: List<ShotGroup>,
   val currentSubgroup: ShotGroup,
)

class CullViewModel(groups: List<ShotGroup>, currentGroup: Int) {

	private val stateFlow_ = MutableStateFlow(CullState(groups, currentGroup))
	val stateFlow = stateFlow_.asStateFlow()

    var currentGroup = mutableStateOf<CullGroup?>(null)

	init {
        updateCurrentGroup()
    }

	private fun updateCurrentGroup() {
        val group = stateFlow.value.groups[stateFlow.value.group]
        val subgroups = group.shots.chunked(4).map { ShotGroup(it) }
        val currentSubgroup = subgroups[stateFlow.value.subgroup]
        currentGroup.value = CullGroup(group, subgroups, currentSubgroup)
    }

	fun onGroupSelected(group: Int) {
		stateFlow_.update { current ->
			CullState(current.groups, group)
		}
		updateCurrentGroup()
	}

	fun onSubGroupSelected(subgroup: Int) {
		stateFlow_.update { current ->
			CullState(current.groups, current.group, subgroup, 0)
		}
		updateCurrentGroup()
	}
}
