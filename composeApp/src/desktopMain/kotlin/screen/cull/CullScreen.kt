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
import component.CullGrid

import androidx.compose.ui.graphics.asImageBitmap
import component.Burst
import database.ShotDatabase
import database.entity.Shot
import kotlinx.coroutines.launch
import org.jetbrains.skia.Image
import shot.ShotGroup
import java.io.File

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
	var clickedButton by remember { mutableStateOf(0) }
	val currentGroup by viewModel.currentGroup
//	val dao = ShotDatabase.instance.dao
	var shotEntity by remember { mutableStateOf<Shot?>(null) }
	var likeState by remember { mutableStateOf("Undefined") }
	val scope = rememberCoroutineScope()

	val imageBitmaps by remember {
		derivedStateOf {
			currentGroup?.let {
				convertShotGroupToImageBitmapList(it.currentSubgroup)
			}
		}
	}

	LaunchedEffect(state.subgroup, state.group) {
		clickedButton = 0
	}

	LaunchedEffect(state.subgroup, clickedButton) {
		if (clickedButton < state.subgroups[state.subgroup].shots.size) {
//			shotEntity = dao.select(state.subgroups[state.subgroup].shots[clickedButton].file.toString())
			likeState = when (shotEntity?.lucky) {
				false -> "Liked"
				true -> "Disliked"
				null -> "Undefined"
			}
		}
	}

	Row(modifier = Modifier.fillMaxSize()) {
		Column(modifier = Modifier.weight(0.7f).fillMaxHeight().background(BoxesColor)) {
			LazyColumn(
				modifier = Modifier.weight(0.7f).fillMaxHeight(1.0f).background(BoxesColor)
			) {
				items(state.groups.size) { index ->
					val images = convertShotGroupToImageBitmapList(state.groups[index])
					Burst(
						modifier = Modifier.clickable { viewModel.onGroupSelected(index) },
						shots = images,
						caption = "Group: $index"
					)
				}
			}

			Box(modifier = Modifier.weight(0.3f).fillMaxHeight().background(BoxesColor)) {
				Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
					if (clickedButton < state.subgroups[state.subgroup].shots.size) {
						Text(
							modifier = Modifier.padding(8.dp)
								.fillMaxWidth()
								.weight(1.0f)
								.align(Alignment.CenterHorizontally),
							text = "Image path: ${state.subgroups[state.subgroup].shots[clickedButton].file}\n Liked the image: ${likeState}\nImage Grid:\ngroup: ${state.group}\nsubgroup: ${state.subgroup}\n Size of the sub Group: ${state.subgroups[state.subgroup].shots.size}\n Current Image: ${clickedButton + 1}\n size of the group: ${state.groups[state.group].shots.size}",
						)
					}
				}
			}
			//TODO: Like Dislike on images (task 3)
			// Если будете это делать, то лучше привязать эту кнопку в CullGrid, чтобы она автоматически рендерилась с картинкой
			Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
				Button(onClick = {
					scope.launch {
						shotEntity?.let {
							// TODO:
							it.lucky = true
//							dao.updatePhoto(it)
//							likeState = "Liked"
						}
					}
				}) {
					Text(text = "Like")
				}

				Button(onClick = {
					scope.launch {
						shotEntity?.let {
							// TODO:
							it.lucky = false
//							dao.updatePhoto(it)
//							likeState = "Disliked"
						}
					}
				}) {
					Text(text = "Dislike")
				}
			}
		}

		Column(modifier = Modifier.weight(4.0f).fillMaxHeight()) {
			Box(
				modifier = Modifier
					.weight(7.0f)
					.fillMaxWidth()
			) {
				imageBitmaps?.let {
					CullGrid(modifier = Modifier.align(Alignment.Center), images = it,
						onButtonClick = { index ->
							clickedButton = index
							viewModel.onCircleButtonClicked(index)
						}
					)
				}
			}

			//TODO: SLIDER (task 2)
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
	}
}