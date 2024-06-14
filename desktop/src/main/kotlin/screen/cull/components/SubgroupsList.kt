package screen.cull.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import shot.ShotGroup
import util.AsyncImage

@Composable
fun SubgroupsList(
	modifier: Modifier = Modifier,
	// TODO: highlight current group
	current: Int,
	subgroups: List<ShotGroup>,
	onSubgroupSelected: (Int) -> Unit,
) {
	LazyRow(modifier = modifier) {
		itemsIndexed(subgroups) { index, subgroup ->
			// Display subgroups
			SubgroupItem(
				subgroup = subgroup,
				onClick = { onSubgroupSelected(index) }
			)
		}
	}
}

@Composable
fun SubgroupItem(
	subgroup: ShotGroup,
	onClick: () -> Unit,
) {
	Box(
		modifier = Modifier.aspectRatio(1.0f)
			.clickable(onClick = onClick),
	) {
		// Display image for each subgroup
		AsyncImage(
			modifier = Modifier.fillMaxSize(),
			file = subgroup.shots[0].file
		)
	}
}
