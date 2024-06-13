package screen.cull

import shot.ShotGroup
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CullViewModel(groups: List<ShotGroup>, currentGroup: Int) {

	private val stateFlow_ = MutableStateFlow(CullState(groups, currentGroup))
	val stateFlow = stateFlow_.asStateFlow()

	fun onGroupSelected(group: Int) {
		stateFlow_.update { current ->
			CullState(current.groups, group)
		}
	}

	fun onSubGroupSelected(subgroup: Int) {
		stateFlow_.update { current ->
			CullState(current.groups, current.group, subgroup, 0)
		}
	}
}
