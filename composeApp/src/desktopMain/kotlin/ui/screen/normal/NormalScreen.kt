package ui.screen.normal

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import ui.stubListOfShotCluster
import util.ShotCluster

@Composable
fun NormalScreen(
	clusters: List<ShotCluster>,
	thumbnail: suspend (Long) -> ImageBitmap?,
) {
	ClusterGrid(
		modifier = Modifier.fillMaxSize(),
		clusters = clusters,
		thumbnail = thumbnail,
	)
}

@Preview
@Composable
fun NormalScreenPreview() {
	val image = remember {
		useResource("icons/icon.png") {
			loadImageBitmap(it)
		}
	}

	NormalScreen(
		clusters = stubListOfShotCluster(),
		thumbnail = {
			image
		}
	)
}
