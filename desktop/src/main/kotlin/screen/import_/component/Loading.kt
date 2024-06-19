package screen.import_.component

import style.Roboto
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BoxScope.Loading(
    dirName: String,
    progress: Float,
    onCancel: () -> Unit,
) {
    Box(modifier = Modifier.align(Alignment.TopCenter)) {
        Text(
        text = "Importing ${dirName}",
        style = MaterialTheme.typography.body1.copy(
                letterSpacing = 0.5.sp,
                fontSize = 32.sp,
                fontFamily = Roboto,
                fontWeight = FontWeight.Medium,
                color = mainColor
        ),
        modifier = Modifier
            .align(Alignment.TopCenter)
            .padding(top = 108.dp)
    )
    }
    Box(
        modifier = Modifier
            .align(Alignment.Center)
    ) {
        CircularProgressIndicator(
            progress = progress,
            strokeWidth = 4.dp,
            modifier = Modifier.size(50.dp),
            color = Color(0xFF21005D),
        )
    }
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(110.dp),
    ) {
        Column {
            LinearProgressIndicator(
                progress = progress,
                color = Color(0xFF21005D),
                modifier = Modifier.size(404.dp, 4.dp)
            )
            Row {
                Spacer(modifier = Modifier.size(45.dp, 16.dp))
                Text(
                    //todo: set how much left
                    text ="text1",
                    style = MaterialTheme.typography.body1.copy(
                        fontSize = 16.sp,
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Medium,
                        color = mainColor
                    )
                )
                Spacer(modifier = Modifier.size(235.dp, 16.dp))
                Text(
                    //todo: set how much time left
                    text = "text2",
                    style = MaterialTheme.typography.body1.copy(
                        fontSize = 16.sp,
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Medium,
                        color = mainColor
                    )
                )
            }
        }
    }
    Box(
        modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(50.dp)
    ) {
        Button(
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFFEADDFF),
            ),
            onClick = onCancel,
            modifier = Modifier.size(150.dp, 32.dp),
            elevation = ButtonDefaults.elevation(0.dp),
            border = BorderStroke(3.dp, Color(0xFF21005D)),
            shape = RoundedCornerShape(30.dp),
        ) {
            Text(
                text = "Cancel",
                MaterialTheme.typography.body2.copy(
                    fontSize = 16.sp,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Medium,
                    color = mainColor
                )
            )
        }
    }    
}

@Composable
@Preview
private fun LoadingPreview() {
    Container(
        state = Unit
    ) {
        Loading(
            dirName = "SomeDir",
            progress = 0.75f,
            onCancel = {},
        )
    }
}
