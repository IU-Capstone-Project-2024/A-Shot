import androidx.compose.ui.awt.ComposeWindow
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

fun selectDirectory(window: ComposeWindow, title: String): Set<File>? {
    val fileChooser = JFileChooser().apply {
        dialogTitle = title
        fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
        fileFilter = FileNameExtensionFilter("Directory", "directory")
    }

    val result = fileChooser.showOpenDialog(window)
    if (result == JFileChooser.APPROVE_OPTION) {
        val selectedDirectory = fileChooser.selectedFile
        val subdirectories = selectedDirectory.listFiles { file -> file.isDirectory } ?: emptyArray()

        return if (subdirectories.isNotEmpty()) {
            subdirectories.toSet()
        } else {
            null
        }
    }
    return null
}
