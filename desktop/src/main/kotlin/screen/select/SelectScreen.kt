package screen.select

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
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
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

private fun selectDirectory(window: ComposeWindow, title: String): File? {
	val fileChooser = JFileChooser().apply {
		dialogTitle = title
		fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
		fileFilter = FileNameExtensionFilter("Directory", "directory")
	}

	val result = fileChooser.showOpenDialog(window)
	if (result == JFileChooser.APPROVE_OPTION) {
		return fileChooser.selectedFile
	}
	return null
}

@Composable
@Preview
fun SelectScreen(window: ComposeWindow, onDirectorySelected: (File?) -> Unit) {
	val coroutineScope = rememberCoroutineScope()

	Box(
		modifier = Modifier.fillMaxSize(),
	) {
		Button(
			modifier = Modifier.align(Alignment.Center),
			onClick = {
				coroutineScope.launch {
					val dir = selectDirectory(window, "Select Directory with Groups")
					onDirectorySelected(dir)
				}
			}
		) {
			Text("Select Directory")
		}
	}
}
