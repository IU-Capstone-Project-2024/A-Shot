package screen.cull

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import screen.cull.components.GroupsList
import screen.cull.components.SubgroupsList
import screen.cull.components.fileToImageBitmap
import androidx.compose.ui.graphics.ImageBitmap
import java.io.File

@Composable
fun CullScreen(viewModel: CullViewModel) {
	val state by viewModel.stateFlow.collectAsState()
	var count by remember { mutableStateOf(0) }
    val imageCache = remember { mutableMapOf<File, ImageBitmap?>() }
	Row(modifier = Modifier.fillMaxSize()) {
		// Left LazyColumn
		GroupsList(
			modifier = Modifier.weight(0.5f).fillMaxHeight(),
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
				Button(
					onClick = { count++ },
					modifier = Modifier.padding(end = 8.dp) // Increment count when button is clicked
				) {
					Text("next img")
				}

				if (count >= state.subgroups[state.subgroup].shots.size) {
					count = 0 // Reset count if it exceeds list size
				}
				fileToImageBitmap(state.subgroups[state.subgroup].shots[count].file, imageCache)?.let {
					Image(
						bitmap = it,
						contentDescription = null,
						modifier = Modifier.fillMaxSize()
					)
				}
			}

			// Bottom Row
			SubgroupsList(
				modifier = Modifier
					.weight(1.0f)
					.fillMaxWidth(),
				current = state.subgroup,
				subgroups = state.subgroups,
				onSubgroupSelected = viewModel::onSubGroupSelected,
                cache = imageCache
			)

			LaunchedEffect(state.subgroup) {
				count = 0
			}
		}

		// Right Box
		Box(modifier = Modifier.weight(1.0f).fillMaxHeight()) {
			Text(text = "Image path: ${state.subgroups[state.subgroup].shots[count].file}")
			Text(
				modifier = Modifier.align(Alignment.Center),
				text = "Image Grid:\ngroup: ${state.group}\nsubgroup: ${state.subgroup}\n Size of the sub Group: ${state.subgroups[state.subgroup].shots.size}\n Current Image: ${count+1}",
			)
		}
	}
	// Other UI elements like photo information and back button will be here
}
