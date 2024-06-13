import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import java.io.File


@Composable
@Preview
fun App(window: ComposeWindow) {
    var currentPage by remember { mutableStateOf("start") }
    var groups by remember { mutableStateOf<Set<File>>(emptySet()) }

    when (currentPage) {
        "start" -> StartPage(onGoidaClicked = { currentPage = "select" })
        "select" -> SelectPage(
            window = window,
            onDirectoriesSelected = { selectedGroups ->
                groups = selectedGroups
                currentPage = "view"
            }
        )
        "view" -> ViewPage(groups = groups, onBack = { currentPage = "start" })
    }
}

fun main() = application {
    val windowState = rememberWindowState(width = 1200.dp, height = 800.dp)
    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "A-Shot",
        icon = painterResource("icons/icon.png")
    ) {
        App(window = window)
    }
}

