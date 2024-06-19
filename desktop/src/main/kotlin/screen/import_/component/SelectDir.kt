package screen.import_.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

private fun selectDirectory(title: String): File? {
    val fileChooser = JFileChooser().apply {
        dialogTitle = title
        fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        fileFilter = FileNameExtensionFilter("Directory", "directory")
    }

    val result = fileChooser.showOpenDialog(null)
    if (result == JFileChooser.APPROVE_OPTION) {
        return fileChooser.selectedFile
    }
    return null
}

@Composable
fun BoxScope.SelectDir(onDirSelected: (File) -> Unit) {
    val scope = rememberCoroutineScope()

    Button(
        modifier = Modifier
            .align(Alignment.Center),
        onClick = {
//			scope.launch {
            val dir = selectDirectory("Select Directory")
            if (dir != null) {
                onDirSelected(dir)
            }
//			}
        }
    ) {
        Text("Select Directory")
    }
}

@Composable
@Preview
private fun SelectDirPreview() {
    Container(
        state = Unit
    ) {
        SelectDir {}
    }
}
