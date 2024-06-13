import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File
import javax.imageio.ImageIO

@Composable
fun ViewPage(groups: Set<File>, onBack: () -> Unit) {

    val folderImage: ImageBitmap = useResource("folder.png") { loadImageBitmap(it) }
    var selectedGroupIndex by remember { mutableStateOf(0) }
    var selectedImageIndex by remember { mutableStateOf(0) }
    var selectedImageFile by remember { mutableStateOf<File?>(null) }
    var selectedSubGroupIndex by remember { mutableStateOf(0) }
    val groupsList = groups.toList()
    val selectedGroup = groupsList[selectedGroupIndex]

    val images = selectedGroup.listFiles { _, name ->
        name.endsWith(".png", ignoreCase = true)
    }?.map { ImageIO.read(it).toImageBitmap() to it } ?: emptyList()

    val subGroups = images.chunked(4)

    val onImageSelected: (Int) -> Unit = { index ->
        selectedImageIndex = index
    }

    selectedImageFile = images.getOrNull(selectedImageIndex)?.second


    Row(modifier = Modifier.fillMaxSize()) {
        // Left LazyColumn
        LazyColumn(modifier = Modifier.weight(1.0f).fillMaxHeight()) {
            items(groupsList) { group ->
                val index = groupsList.indexOf(group)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth().fillMaxHeight()
                ) {
                    // Display the folder image for each group
                    Image(
                        bitmap = folderImage,
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(vertical = 8.dp)
                            .border(1.dp, if (selectedGroupIndex == index) Color.Green else Color.Transparent)
                            .clickable {
                                selectedGroupIndex = index
                                selectedImageIndex = 0 // Reset the image selection when the group changes
                            }
                    )
                    Text(
                        text = group.name,
                        color = Color.Black,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
        }

        // Center Column
        Column(modifier = Modifier.weight(4.0f).fillMaxHeight()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(7.0f)
            ) {

                ImageGrid(imageBitmapList = getBitmapsFromSelectedSubGroup(
                    subGroups, selectedSubGroupIndex),onImageSelected = onImageSelected
                )
            }

            // Bottom Row
            LazyRow(modifier = Modifier.fillMaxWidth().weight(1.0f)) {
                items(subGroups.size) { index ->
                    // Display subgroups
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(2.dp, if (selectedSubGroupIndex == index) Color.Green else Color.Transparent)
                            .clickable {
                                selectedSubGroupIndex = index
                                selectedImageIndex = 0 // Reset the image index when a subgroup is clicked
                            }
                            .padding(8.dp)
                    ) {
                        // Display image for each subgroup
                        if (subGroups[index].isNotEmpty()) {
                            val (bitmap, _) = subGroups[index][0]
                            Image(
                                bitmap = bitmap,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }

        // Right Box
        Box(
            modifier = Modifier
                .weight(1.0f)
                .fillMaxHeight()
                //.background(Color.Blue)
        ) {
            Text(text = "Selected Image Index: $selectedImageIndex")
            Text(text = "File name: $selectedImageIndex")
        }
    }
    // Other UI elements like photo information and back button  will be here
}