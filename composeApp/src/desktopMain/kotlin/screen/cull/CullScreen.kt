package screen.cull

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.ui.unit.sp
import component.Burst
import database.Photo
import database.getDatabaseBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
	val dao = getDatabaseBuilder().dao
	var photo by remember { mutableStateOf<Photo?>(null) }
	var likeState by remember { mutableStateOf("Undefined") }
	val scope = rememberCoroutineScope()

    val imageBitmaps by remember {
        derivedStateOf {
            currentGroup?.let {
                convertShotGroupToImageBitmapList(it.currentSubgroup)
            }
        }
    }

	Row(modifier = Modifier.fillMaxSize()) {
		Column(modifier = Modifier.weight(0.7f).fillMaxHeight().background(BoxesColor)){
			LazyColumn(
				modifier = Modifier.weight(0.7f).fillMaxHeight(1.0f).background(BoxesColor)
			) {
				items(state.groups.size) { index ->
					val images = convertShotGroupToImageBitmapList(state.groups[index])
					Burst(
						// TODO: move the click logic to the Burst.kt itself, when it will be ready for deployment
						//  Also, the LazyColumn should be moved to the Burst.kt
						modifier = Modifier.clickable { viewModel.onGroupSelected(index) },
						shots = images,
						caption = "Group: $index"
					)
				}
			}

			LaunchedEffect(state.subgroup, state.subgroups[state.subgroup].shots[count].file) {
				photo = dao.getByPath(state.subgroups[state.subgroup].shots[count].file.toString())
				val goida = photo?.isGood
				likeState = when (goida) {
					true -> "Liked"
					false -> "Disliked"
					null -> "Undefined"
				}
			}

			// Right Box
			Box(modifier = Modifier.weight(0.3f).fillMaxHeight().background(BoxesColor)) {
				Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
					// Text takes the bottom of Left Column (Exif later)
					Text(
						modifier = Modifier.padding(8.dp)
							.fillMaxWidth()
							.weight(1.0f)
							.align(Alignment.CenterHorizontally),

						text = "Image path: ${state.subgroups[state.subgroup].shots[count].file}\n Liked the image: ${likeState}\nImage Grid:\ngroup: ${state.group}\nsubgroup: ${state.subgroup}\n Size of the sub Group: ${state.subgroups[state.subgroup].shots.size}\n Current Image: ${count + 1}\n size of the group: ${state.groups[state.group].shots.size}",
					)
				}

			}
			// Row for Like and Dislike buttons
			//TODO: Move this logic to other UI element on the screen
			Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
				Button(onClick = {
					scope.launch {
						photo?.let {
							it.isGood = true
							dao.updatePhoto(it)
							likeState = "Liked"
						}
					}
				}) {
					Text(text = "Like")
				}

				Button(onClick = {
					scope.launch {
						photo?.let {
							it.isGood = false
							dao.updatePhoto(it)
							likeState = "Disliked"
						}
					}
				}) {
					Text(text = "Dislike")
				}
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
					//TODO: make the button grid,
					// that chooses the num and location of buttons with respect to num of images
					//ButtonGrid(modifier = Modifier.align(Alignment.Center))
                }

				// Circular buttons to change the selected image
				Row(
					modifier = Modifier
						.align(Alignment.Center)
						.padding(8.dp),
					horizontalArrangement = Arrangement.SpaceEvenly
				) {
					CircleButton(onClick = { count = 0}, label = "1")
					CircleButton(onClick = { count = 1}, label = "2")
					CircleButton(onClick = { count = 2}, label = "3")
					CircleButton(onClick = { count = 3}, label = "4")
				}
			}
			//TODO: make this as slider(as presented in Figma design)
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
				LaunchedEffect(state.subgroup) {
					count = 0
				}
				LaunchedEffect(state.group) {
					count = 0
				}
			}
		}

	}
	// Other UI elements like photo information and back button will be here
}


@Composable
fun CircleButton(onClick: () -> Unit, label: String) {
	Button(
		onClick = onClick,
		shape = CircleShape,
		modifier = Modifier
			.size(60.dp)
			.border(2.dp, Color.Black, CircleShape)
	) {
		Text(text = label, fontSize = 12.sp, color = Color.White)
	}
}