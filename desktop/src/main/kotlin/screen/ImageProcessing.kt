package screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp

@Composable
fun ImageGrid(imageBitmapList: List<ImageBitmap>, onImageSelected: (Int) -> Unit) {
    val selectedIndex by remember { mutableStateOf(-1) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        when (imageBitmapList.size) {
            1 -> SingleImage(imageBitmapList[0], selectedIndex == 0) { onImageSelected(0) }
            2 -> TwoImages(imageBitmapList, selectedIndex) { onImageSelected(it) }
            3 -> ThreeImages(imageBitmapList, selectedIndex) { onImageSelected(it) }
            4 -> FourImages(imageBitmapList, selectedIndex) { onImageSelected(it) }
        }
    }
}
@Composable
fun SingleImage(imageBitmap: ImageBitmap, isSelected: Boolean, onClick: () -> Unit) {
    ImageBox(imageBitmap = imageBitmap, onClick, modifier = Modifier.fillMaxSize())
}

@Composable
fun TwoImages(imageBitmapList: List<ImageBitmap>, selectedIndex: Int, onClick: (Int) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        ImageBox(imageBitmap = imageBitmapList[0], onClick = { onClick(0) }, modifier = Modifier.weight(1f))
        ImageBox(imageBitmap = imageBitmapList[1], onClick = { onClick(1) }, modifier = Modifier.weight(1f))
    }
}

@Composable
fun ThreeImages(imageBitmapList: List<ImageBitmap>, selectedIndex: Int, onClick: (Int) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.weight(1f)) {
            ImageBox(imageBitmap = imageBitmapList[0], onClick = { onClick(0) }, modifier = Modifier.weight(1f))
            ImageBox(imageBitmap = imageBitmapList[1], onClick = { onClick(1) }, modifier = Modifier.weight(1f))
        }
        ImageBox(imageBitmap = imageBitmapList[2], onClick = { onClick(2) }, modifier = Modifier.weight(1f))
    }
}

@Composable
fun FourImages(imageBitmapList: List<ImageBitmap>, selectedIndex: Int, onClick: (Int) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.weight(1f)) {
            ImageBox(imageBitmap = imageBitmapList[0], onClick = { onClick(0) }, modifier = Modifier.weight(1f))
            ImageBox(imageBitmap = imageBitmapList[1], onClick = { onClick(1) }, modifier = Modifier.weight(1f))
        }
        Row(modifier = Modifier.weight(1f)) {
            ImageBox(imageBitmap = imageBitmapList[2], onClick = { onClick(2) }, modifier = Modifier.weight(1f))
            ImageBox(imageBitmap = imageBitmapList[3], onClick = { onClick(3) }, modifier = Modifier.weight(1f))
        }
    }
}


@Composable
fun ImageBox(imageBitmap: ImageBitmap, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
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
