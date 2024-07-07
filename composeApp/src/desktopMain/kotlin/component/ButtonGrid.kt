package component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ButtonGrid(
    modifier: Modifier = Modifier,
    images: List<ImageBitmap>,
    onClick: (Int) -> Unit
) {
    when (images.size) {
        2 -> ButtonGrid2(modifier, onClick)
        4 -> ButtonGrid4(modifier, onClick)
        else -> {
            // TODO: error
        }
    }
}

@Composable
fun ButtonGrid2(
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            CircleButton(onClick = { onClick(0) }, label = "1")
            Spacer(modifier = Modifier.width(16.dp))
            CircleButton(onClick = { onClick(1) }, label = "2")
        }
    }
}

@Composable
fun ButtonGrid4(
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CircleButton(onClick = { onClick(0) }, label = "1")
            CircleButton(onClick = { onClick(1) }, label = "2")
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CircleButton(onClick = { onClick(2) }, label = "3")
            CircleButton(onClick = { onClick(3) }, label = "4")
        }
    }
}

@Composable
fun CircleButton(onClick: () -> Unit, label: String) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        modifier = Modifier
            .size(60.dp)
            .padding(8.dp)
    ) {
        Text(text = label, fontSize = 12.sp, color = Color.White)
    }
}
