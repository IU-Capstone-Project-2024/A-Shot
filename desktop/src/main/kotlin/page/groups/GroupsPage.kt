package page.groups

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import shot.ShotGroup
import util.AsyncImage
import java.awt.Dimension

@Composable
fun GroupsPage(
	groups: List<ShotGroup>,
	onGroupClick: (Int) -> Unit,
) {
	LazyVerticalGrid(
		modifier = Modifier.fillMaxSize(),
		columns = GridCells.Adaptive(120.dp),
	) {
		itemsIndexed(
			items = groups,
			key = { _, group -> group }
		) { index, group ->
			GroupedItem(
				group = group,
				onClick = { onGroupClick(index) },
			)
		}
	}
}

@Composable
fun GroupedItem(
	group: ShotGroup,
	onClick: () -> Unit,
) {
	Card(
		modifier = Modifier.aspectRatio(1f)
			.clickable(onClick = onClick),
	) {
		AsyncImage(
			modifier = Modifier.fillMaxSize(),
			file = group.shots[0].file,
			dimension = Dimension(300, 300),
		)
		Text(
			text = "${group.shots.size} items"
		)
	}
}
