import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun SelectPage(window: ComposeWindow, onDirectoriesSelected: (Set<File>) -> Unit) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            coroutineScope.launch {
                val chosenDirectories = selectDirectory(window, "Select Directory with Groups")
                if (!chosenDirectories.isNullOrEmpty()) {
                    onDirectoriesSelected(chosenDirectories)
                } else {
                    // Show dialog if no subdirectories are found
                }
            }
        }) {
            Text("Select Directory")
        }
    }
}