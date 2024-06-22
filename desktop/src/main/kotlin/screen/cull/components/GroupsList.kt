package screen.cull.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.ResourceLoader
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import shot.ShotGroup
import util.AsyncImage
import java.awt.Dimension
import java.nio.file.Paths

@Composable
fun GroupsList(
	modifier: Modifier = Modifier,
	// TODO: highlight current group
	current: Int,
	groups: List<ShotGroup>,
	onGroupSelected: (Int) -> Unit,
) {
	LazyColumn(
		modifier = modifier
	) {
		itemsIndexed(groups) { index, group ->
			GroupItem(
				index = index,
				group = group,
				onClick = { onGroupSelected(index) },
			)
		}
	}
}

// TODO: seems to be duplicate with SubgroupItem
@Composable
fun GroupItem(
	index: Int,
	group: ShotGroup,
	onClick: () -> Unit,
) {
	Box(
		modifier = Modifier.aspectRatio(1.0f).padding(8.dp)
			.clickable(onClick = onClick)
	) {
		// Display the folder image for each group
		AsyncImage(
			modifier = Modifier.fillMaxSize(),
			file = Paths.get("src/main/resources/stubs/putin.jpg").toAbsolutePath().toFile(),
			dimension = Dimension(300, 300),
		)

		Text(
			text = "Group #$index",
			color = Color.Black,
			fontSize = 12.sp,
			textAlign = TextAlign.Center,
			modifier = Modifier
				.fillMaxWidth()
				.align(Alignment.Center)
		)
	}
}
