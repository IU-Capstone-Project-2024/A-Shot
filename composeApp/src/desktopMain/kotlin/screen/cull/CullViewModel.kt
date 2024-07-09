package screen.cull

import androidx.compose.runtime.mutableStateOf
import shot.ShotGroup
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CullGroup (
   val group: ShotGroup,
   val subgroups: List<ShotGroup>,
   val currentSubgroup: ShotGroup,
)

class CullViewModel(groups: List<ShotGroup>, currentGroup: Int) {

	private val stateFlow_ = MutableStateFlow(CullState(groups, currentGroup))
	val stateFlow = stateFlow_.asStateFlow()

	private var groups = mutableStateOf<List<ShotGroup>>(emptyList())
    var currentGroup = mutableStateOf<CullGroup?>(null)

    init {
        updateCurrentGroup()
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

    fun nextGroup() {
        stateFlow_.update { current ->
            val nextGroup = (current.group + 1) % current.groups.size
            CullState(current.groups, nextGroup)
        }
        updateCurrentGroup()
    }

    fun prevGroup() {
        stateFlow_.update { current ->
            val prevGroup = if (current.group - 1 < 0) current.groups.size - 1 else current.group - 1
            CullState(current.groups, prevGroup)
        }
        updateCurrentGroup()
    }

    fun nextSubgroup() {
        stateFlow_.update { current ->
            val nextSubgroup = (current.subgroup + 1) % current.subgroups.size
            CullState(current.groups, current.group, nextSubgroup,0)
        }
        updateCurrentGroup()
    }

    fun prevSubgroup() {
        stateFlow_.update { current ->
            val prevSubgroup = if (current.subgroup - 1 < 0) current.subgroups.size - 1 else current.subgroup - 1
            CullState(current.groups, current.group, prevSubgroup,0)
        }
        updateCurrentGroup()
    }

    fun onCircleButtonClicked(index: Int) {
        stateFlow_.update { current ->
            CullState(current.groups, current.group, current.subgroup, index)
        }
    }


    private fun updateCurrentGroup() {
        val group = stateFlow.value.groups[stateFlow.value.group]
        val subgroups = group.shots.chunked(4).map { ShotGroup(it) }
        val currentSubgroup = subgroups[stateFlow.value.subgroup]
        currentGroup.value = CullGroup(group, subgroups, currentSubgroup)
    }
}
