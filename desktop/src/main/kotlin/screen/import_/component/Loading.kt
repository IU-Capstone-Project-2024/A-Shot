package screen.import_.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BoxScope.Loading(
    dirName: String,
    progress: Float,
    onCancel: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .align(Alignment.Center),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Importing ${dirName}")
        Spacer(modifier = Modifier.height(40.dp))
        LinearProgressIndicator(progress = progress)
        Spacer(modifier = Modifier.height(40.dp))
        Button(onClick = onCancel) {
            Text(text = "Cancel")
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
