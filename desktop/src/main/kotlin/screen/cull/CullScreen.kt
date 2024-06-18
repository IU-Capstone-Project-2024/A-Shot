package screen.cull

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import screen.cull.components.GroupsList
import screen.cull.components.SubgroupsList
import component.CullGrid
import androidx.compose.ui.graphics.asImageBitmap
import component.Burst
import org.jetbrains.skia.Image
import shot.ShotGroup
import java.io.File
import java.io.FileInputStream

val BoxesColor = Color(0xFFEADDFF)

fun convertShotGroupToImageBitmapList(shotGroup: ShotGroup): List<ImageBitmap> {
    return shotGroup.shots.mapNotNull { shot ->
        convertFileToImageBitmap(shot.file)
    }
}

private fun convertFileToImageBitmap(file: File): ImageBitmap? {
    return try {
        val byteArray = file.readBytes()
        val image = Image.makeFromEncoded(byteArray)
        image.asImageBitmap()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Composable
fun CullScreen(viewModel: CullViewModel) {
	val state by viewModel.stateFlow.collectAsState()
	var count by remember { mutableStateOf(0) }
	val currentGroup by viewModel.currentGroup
    //val imageCache = remember { mutableMapOf<File, ImageBitmap?>() }


    val imageBitmaps by remember {
        derivedStateOf {
            currentGroup?.let {
                convertShotGroupToImageBitmapList(it.currentSubgroup)
            }
        }
    }

	Row(modifier = Modifier.fillMaxSize()) {
		// Left LazyColumn
		/*GroupsList(
			modifier = Modifier.weight(1f).fillMaxHeight().background(BoxesColor),
			current = state.group,
			groups = state.groups,
			onGroupSelected = viewModel::onGroupSelected
		)*/

		// With the Burst the app seems to be lagging a bit, just comment it and use GroupList if you want
		// Left LazyColumn with multiple Burst components
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxHeight().background(BoxesColor)
        ) {
            items(state.groups.size) { index ->
                val images = convertShotGroupToImageBitmapList(state.groups[index])
                Burst(
					// TODO: move the click logic to the Burst.kt itself, when it will be ready for deployment
					modifier = Modifier.clickable { viewModel.onGroupSelected(index) },
                    shots = images,
                    caption = "Group: $index"
                )
            }
        }
        // Center Column
        Column(modifier = Modifier.weight(4.0f).fillMaxHeight()) {
			Box(
				modifier = Modifier
					.weight(7.0f)
					.fillMaxWidth()
			) {
				imageBitmaps?.let {
                    CullGrid(modifier = Modifier.align(Alignment.Center), images = it)
                }
			}
			// Navigation buttons (Just for simple clicking and debug)
			Row(
				modifier = Modifier.fillMaxWidth().padding(8.dp),
				horizontalArrangement = Arrangement.SpaceBetween
			) {
				Button(onClick = { viewModel.prevGroup() }) {
					Text("Previous Group")
				}
				Button(onClick = { viewModel.nextGroup() }) {
					Text("Next Group")
				}
				Button(onClick = { viewModel.prevSubgroup() }) {
					Text("Previous Subgroup")
				}
				Button(onClick = { viewModel.nextSubgroup() }) {
					Text("Next Subgroup")
				}
			}
		}
		// Right Box
		Box(modifier = Modifier.weight(1.0f).fillMaxHeight().background(BoxesColor)) {
			Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
				// SubGroupList takes the upper half
				SubgroupsList(
					modifier = Modifier
						.fillMaxWidth()
						.weight(1.0f),
					current = state.subgroup,
					subgroups = state.subgroups,
					onSubgroupSelected = viewModel::onSubGroupSelected,
				)

				// Text takes the bottom half (Exif later)
				Text(
					modifier = Modifier.padding(8.dp)
						.fillMaxWidth()
						.weight(1.0f)
						.align(Alignment.CenterHorizontally),
					text = "Image path: ${state.subgroups[state.subgroup].shots[count].file} Image Grid:\ngroup: ${state.group}\nsubgroup: ${state.subgroup}\n Size of the sub Group: ${state.subgroups[state.subgroup].shots.size}\n Current Image: ${count + 1}\n size of the group: ${state.groups[state.group].shots.size}",
				)
			}

			LaunchedEffect(state.subgroup) {
				count = 0
			}
		}
	}
	// Other UI elements like photo information and back button will be here
}