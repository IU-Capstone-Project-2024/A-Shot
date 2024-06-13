import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.*
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import page.SelectPage
import page.StartPage
import page.ViewPage
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
    val windowState = rememberWindowState(
		placement = WindowPlacement.Maximized,
		position = WindowPosition.PlatformDefault,
	)

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "A-Shot",
        icon = painterResource("icons/icon.png")
    ) {
        App(window = window)
    }
}

