import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import screen.main.MainScreen
import screen.select.SelectScreen
import java.io.File

sealed class Screen(val route: String) {
	data object Select : Screen("select")
	data object Main : Screen("main")
}

@Composable
@Preview
fun App(window: ComposeWindow) {
	val navController = rememberNavController()
	var selectedDir by remember { mutableStateOf<File?>(null) }

	NavHost(
		modifier = Modifier.fillMaxSize(),
		navController = navController,
		startDestination = Screen.Select.route,
	) {
		composable(
			route = Screen.Select.route,
		) {
			SelectScreen(
				window = window,
				onDirectorySelected = { dir ->
					selectedDir = dir
					navController.navigate(Screen.Main.route)
				}
			)
		}

		composable(
			route = Screen.Main.route,
		) {
			fun back() {
				selectedDir = null
				navController.popBackStack(Screen.Select.route, false)
			}

			val dir = selectedDir
			if (dir != null && dir.isDirectory) {
				MainScreen(
					dir = dir
				)
			} else {
				// TODO: alert!
				back()
			}
		}
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
