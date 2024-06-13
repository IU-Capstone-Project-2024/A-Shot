import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable
fun ImageGrid(imageBitmapList: List<ImageBitmap>, onImageSelected: (Int) -> Unit) {
    var selectedIndex by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when (imageBitmapList.size) {
            1 -> SingleImage(imageBitmapList[0], selectedIndex == 0) {
                selectedIndex = 0
                onImageSelected(0)
            }
            2 -> TwoImages(imageBitmapList, selectedIndex) { index ->
                selectedIndex = index
                onImageSelected(index)
            }
            3 -> ThreeImages(imageBitmapList, selectedIndex) { index ->
                selectedIndex = index
                onImageSelected(index)
            }
            4 -> FourImages(imageBitmapList, selectedIndex) { index ->
                selectedIndex = index
                onImageSelected(index)
            }
        }
    }
}

@Composable
fun SingleImage(imageBitmap: ImageBitmap, isSelected: Boolean, onClick: () -> Unit) {
    ImageBox(imageBitmap = imageBitmap, isSelected = isSelected, onClick, modifier = Modifier.fillMaxSize())
}

@Composable
fun TwoImages(imageBitmapList: List<ImageBitmap>, selectedIndex: Int, onClick: (Int) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        ImageBox(imageBitmap = imageBitmapList[0], isSelected = selectedIndex == 0, onClick = { onClick(0) }, modifier = Modifier.weight(1f))
        ImageBox(imageBitmap = imageBitmapList[1], isSelected = selectedIndex == 1, onClick = { onClick(1) }, modifier = Modifier.weight(1f))
    }
}

@Composable
fun ThreeImages(imageBitmapList: List<ImageBitmap>, selectedIndex: Int, onClick: (Int) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.weight(1f)) {
            ImageBox(imageBitmap = imageBitmapList[0], isSelected = selectedIndex == 0, onClick = { onClick(0) }, modifier = Modifier.weight(1f))
            ImageBox(imageBitmap = imageBitmapList[1], isSelected = selectedIndex == 1, onClick = { onClick(1) }, modifier = Modifier.weight(1f))
        }
        ImageBox(imageBitmap = imageBitmapList[2], isSelected = selectedIndex == 2, onClick = { onClick(2) }, modifier = Modifier.weight(1f))
    }
}

@Composable
fun FourImages(imageBitmapList: List<ImageBitmap>, selectedIndex: Int, onClick: (Int) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.weight(1f)) {
            ImageBox(imageBitmap = imageBitmapList[0], isSelected = selectedIndex == 0, onClick = { onClick(0) }, modifier = Modifier.weight(1f))
            ImageBox(imageBitmap = imageBitmapList[1], isSelected = selectedIndex == 1, onClick = { onClick(1) }, modifier = Modifier.weight(1f))
        }
        Row(modifier = Modifier.weight(1f)) {
            ImageBox(imageBitmap = imageBitmapList[2], isSelected = selectedIndex == 2, onClick = { onClick(2) }, modifier = Modifier.weight(1f))
            ImageBox(imageBitmap = imageBitmapList[3], isSelected = selectedIndex == 3, onClick = { onClick(3) }, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun ImageBox(imageBitmap: ImageBitmap, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val borderModifier = if (isSelected) {
        Modifier.border(BorderStroke(4.dp, Color.Green))
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .then(borderModifier)
            .padding(4.dp)
            .pointerInput(Unit) {
                detectTapGestures {
                    onClick()
                }
            }
    ) {
        Image(
            bitmap = imageBitmap,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize()
        )
    }
}