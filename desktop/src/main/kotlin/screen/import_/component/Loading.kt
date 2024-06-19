package screen.import_.component

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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .align(Alignment.Center)
    ) {
        Text(
            text = "Importing ${dirName}",
            style = MaterialTheme.typography.button.copy(
                fontSize = 32.sp
            ),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 75.dp)
        )
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
                .padding(100.dp),
        ) {
            Column {
                LinearProgressIndicator(
                    progress = progress,
                    color = Color(0xFF21005D),
                    modifier = Modifier.size(404.dp, 4.dp)
                )
                Row {
                    Spacer(modifier = Modifier.size(50.dp, 10.dp))
                    Text(
                        //todo: set how much left
                        text ="text1",
                        style = MaterialTheme.typography.button.copy(
                            fontSize = 13.sp
                        )
                    )
                    Spacer(modifier = Modifier.size(250.dp, 10.dp))
                    Text(
                        //todo: set how much time left
                        text = "text2",
                        style = MaterialTheme.typography.button.copy(
                            fontSize = 13.sp
                        ))
                    Spacer(modifier = Modifier.size(50.dp, 10.dp))
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(30.dp)
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFEADDFF),
                ),
                onClick = onCancel,
                modifier = Modifier.size(100.dp, 32.dp),
                elevation = ButtonDefaults.elevation(0.dp),
                border = BorderStroke(3.dp, Color(0xFF21005D)),
                shape = RoundedCornerShape(30.dp),
            ) {
                Text(
                    text = "Cancel",
                    style = MaterialTheme.typography.button.copy(
                        fontSize = 13.sp
                    )
                    //todo: fix font
                )
            }
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
