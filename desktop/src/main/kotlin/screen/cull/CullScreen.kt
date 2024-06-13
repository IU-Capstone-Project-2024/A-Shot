package screen.cull

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import screen.cull.components.GroupsList
import screen.cull.components.SubgroupsList

@Composable
fun CullScreen(viewModel: CullViewModel) {
	val state by viewModel.stateFlow.collectAsState()

	Row(modifier = Modifier.fillMaxSize()) {
		// Left LazyColumn
		GroupsList(
			modifier = Modifier.weight(1f).fillMaxHeight(),
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
				Text(
					modifier = Modifier.align(Alignment.Center),
					text = "Image Grid:\ngroup: ${state.group}\nsubgroup: ${state.subgroup}",
				)
			}

			// Bottom Row
			SubgroupsList(
				modifier = Modifier
					.weight(1.0f)
					.fillMaxWidth(),
				current = state.subgroup,
				subgroups = state.subgroups,
				onSubgroupSelected = viewModel::onSubGroupSelected
			)
		}

		// Right Box
		Box(modifier = Modifier.weight(1.0f).fillMaxHeight()) {
			Text(text = "Selected Image Index: ${state.image}")
		}
	}
	// Other UI elements like photo information and back button  will be here
}

