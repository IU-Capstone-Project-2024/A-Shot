package page.ok

import PhotoGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OkPage(
	groups: Array<PhotoGroup>,
	onGroupClick: (PhotoGroup) -> Unit,
) {
	LazyVerticalGrid(
		modifier = Modifier.fillMaxSize(),
		columns = GridCells.Adaptive(120.dp),
	) {
		items(groups.size) {
			val group = groups[it]
			OkItem(
				group = group,
				onClick = onGroupClick,
			)
		}
	}
}

@Composable
fun OkItem(
	group: PhotoGroup,
	onClick: (PhotoGroup) -> Unit = {}
) {
	Card(
		modifier = Modifier.aspectRatio(1f)
			.clickable { onClick(group) },
	) {
		Text(
			text = "${group.files.size} items"
		)
	}
}
