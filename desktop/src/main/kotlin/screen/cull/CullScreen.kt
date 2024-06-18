package screen.cull

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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

	Row(modifier = Modifier.fillMaxSize()) {
		// Left LazyColumn
		GroupsList(
			modifier = Modifier.weight(1f).fillMaxHeight().background(BoxesColor),
			current = state.group,
			groups = state.groups,
			onGroupSelected = viewModel::onGroupSelected
		)

		// Center Column
		Column(modifier = Modifier.weight(4.0f).fillMaxHeight()) {
			Box(
				modifier = Modifier
					.weight(7.0f)
					.fillMaxWidth()
			) {
				currentGroup?.let {
                    val imageBitmaps = convertShotGroupToImageBitmapList(it.currentSubgroup)
                    CullGrid(modifier = Modifier.align(Alignment.Center), images = imageBitmaps)
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
					text = "Image path: ${state.subgroups[state.subgroup].shots[count].file} Image Grid:\ngroup: ${state.group}\nsubgroup: ${state.subgroup}\n Size of the sub Group: ${state.subgroups[state.subgroup].shots.size}\n Current Image: ${count + 1}",
				)
			}

			LaunchedEffect(state.subgroup) {
				count = 0
			}
		}
	}
	// Other UI elements like photo information and back button will be here
}