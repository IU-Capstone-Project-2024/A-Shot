package screen.import_.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BoxScope.Failure(
    reason: String,
    onOk: () -> Unit,
    failureButtonColor: Color = Color(0xFFFFD8E4),
    // todo: add font and fix paddings
) {
    Button(
        colors = ButtonDefaults.buttonColors(
            backgroundColor = failureButtonColor,
            contentColor = Color.Black
        ),
        elevation = ButtonDefaults.elevation(0.dp),
        border = BorderStroke(0.dp, failureButtonColor),
        shape = RoundedCornerShape(40.dp),
        modifier = Modifier
            .fillMaxSize(),
        onClick = onOk
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = reason,
                maxLines = 2,
                style = MaterialTheme.typography.button.copy(
                    fontSize = 32.sp
                ),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 75.dp)
                overflow = TextOverflow.Ellipsis
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(100.dp)
            ) {
                Image(
                    painter = painterResource("drawable/error_icon_v1.0.png"),
                    contentDescription = "Icon",
                    modifier = Modifier
                        .size(100.dp)
                )
            }
        }
    }
}


@Composable
@Preview
private fun FailurePreview() {
    Container(
        state = Unit
    ) {
        Failure(
            reason = "No images found in the folder",
            onOk = { }
        )
    }
}
