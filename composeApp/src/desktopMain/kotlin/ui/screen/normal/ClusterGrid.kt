package ui.screen.normal

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import ui.component.Cluster
import ui.stubListOfShotCluster
import util.ShotCluster

@Composable
fun ClusterGrid(
	modifier: Modifier,
	clusters: List<ShotCluster>,
	thumbnail: suspend (Long) -> ImageBitmap?,
) {
	LazyVerticalGrid(
		modifier = modifier,
		columns = GridCells.Adaptive(256.dp),
		contentPadding = PaddingValues(16.dp),
		verticalArrangement = Arrangement.spacedBy(8.dp),
		horizontalArrangement = Arrangement.spacedBy(8.dp)
	) {
		items(
			items = clusters,
			key = { item -> item.id },
		) { cluster ->
			Cluster(
				modifier = Modifier
					.aspectRatio(1f),
				cluster = cluster,
				thumbnail = thumbnail,
			)
		}
	}
}

@Preview
@Composable
fun ClusterGridPreview() {
	val image = remember {
		useResource("icons/icon.png") {
			loadImageBitmap(it)
		}
	}

	ClusterGrid(
		modifier = Modifier.fillMaxSize(),
		clusters = stubListOfShotCluster(),
		thumbnail = {
			image
		}
	)
}
