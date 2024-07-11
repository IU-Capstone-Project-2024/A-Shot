package page.tailpond

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
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
import shot.Shot_
import util.AsyncImage
import java.awt.Dimension

@Composable
fun TailpondPage(
	shots: List<Shot_>,
	onShotClick: (Int) -> Unit,
) {
	LazyVerticalGrid(
		modifier = Modifier.fillMaxSize(),
		columns = GridCells.Adaptive(256.dp),
		contentPadding = PaddingValues(16.dp),
		horizontalArrangement = Arrangement.spacedBy(16.dp),
		verticalArrangement = Arrangement.spacedBy(16.dp),
	) {
		itemsIndexed(
			items = shots,
			key = { _, item -> item },
		) { index, shot ->
			TailpondItem(
				shot = shot,
				onClick = { onShotClick(index) },
			)
		}
	}
}

@Composable
fun TailpondItem(
	shot: Shot_,
	onClick: () -> Unit
) {
	Card(
		modifier = Modifier.aspectRatio(1f)
			.clickable(onClick = onClick),
	) {
		AsyncImage(
			modifier = Modifier.fillMaxSize(),
			file = shot.file,
			dimension = Dimension(300, 300),
		)

		Text(
			text = "${shot.file.name}!"
		)
	}
}
