package ui.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.stubShotCluster
import util.ShotCluster
import kotlin.math.max
import kotlin.math.min

@Composable
fun Cluster(
	modifier: Modifier = Modifier,
	cluster: ShotCluster,
	thumbnail: suspend (Long) -> ImageBitmap?,
	maxN: Int = 3,
) {
	BoxWithConstraints(
		modifier = modifier.aspectRatio(1f)
	) {
		val containerSize = min(constraints.maxWidth, constraints.maxHeight)
		val space = containerSize * 0.1f
		val contentSize = containerSize - space * max(0, min(cluster.shots.size, maxN) - 1)
		val bottomOffset = (containerSize - contentSize).toInt()

		cluster.shots
			.take(maxN)
			.forEachIndexed { index, shotId ->
				val bitmap by produceState<ImageBitmap?>(null, shotId) {
					launch(Dispatchers.IO) {
						value = thumbnail(shotId)
					}
				}

				Shot(
					modifier = Modifier
						.requiredSize(contentSize.dp)
						.offset {
							IntOffset(
								x = (space * index).toInt(),
								y = bottomOffset - (space * index).toInt(),
							)
						}
						.zIndex(maxN - 1 - index.toFloat()),
					image = bitmap
				)
			}
	}
}

@Preview
@Composable
fun ClusterPreview() {
	Cluster(
		modifier = Modifier.wrapContentSize(),
		cluster = stubShotCluster(),
		thumbnail = {
			useResource("icons/icon.png") {
				loadImageBitmap(it)
			}
		}
	)
}
