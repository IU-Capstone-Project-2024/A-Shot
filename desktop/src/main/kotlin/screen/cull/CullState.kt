package screen.cull

import shot.ShotGroup

class CullState(
	val groups: List<ShotGroup>,
	var group: Int = 0,
	var subgroup: Int = 0,
	var image: Int = 0,
) {

	var subgroups: List<ShotGroup>

	init {
		group = group.coerceIn(0, groups.size - 1)
		subgroups = groups.getOrNull(group)
			?.let { it.shots.chunked(4).map { group -> ShotGroup(group) } }
			?: emptyList()

		subgroup = subgroup.coerceIn(0, groups.size - 1)
		val maxImage = subgroups.getOrNull(subgroup)
			?.let { it.shots.size - 1 }
			?: 0

		image = image.coerceIn(0, maxImage)
	}
}
