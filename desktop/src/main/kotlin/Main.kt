import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import screen.cull.CullScreen
import screen.cull.CullViewModel
import screen.importing.ImportScreen
import screen.overview.OverviewScreen
import screen.select.SelectScreen

enum class Screen {
	Select,
	Import,
	Overview,  // For future usage
	Cull,
}

@Composable
@Preview
fun App(window: ComposeWindow) {
	val model = remember { MainModel() }
	val navController = rememberNavController()
	val state by model.stateFlow.collectAsState()

	fun backToSelect() {
		model.reset()
		navController.popBackStack(Screen.Select.name, false)
	}

	NavHost(
		modifier = Modifier.fillMaxSize(),
		navController = navController,
		startDestination = Screen.Select.name,
	) {
		composable(Screen.Select.name) {
			SelectScreen(
				window = window,
				onDirectorySelected = { dir ->
					if (dir == null) {
						TODO("Report error and recover")
					} else {
						model.selected(dir)
						navController.navigate(Screen.Import.name)
					}
				},
			)
		}

		composable(Screen.Import.name) {
			val dir = state.dir
			if (dir == null) {
				backToSelect()
				return@composable
			}

			ImportScreen(
				dir = dir,
				onImported = { collection ->
					model.overview(collection)
					navController.navigate(Screen.Cull.name) {
						popUpTo(Screen.Select.name) { inclusive = false }
					}
				},
				onClose = {
					backToSelect()
				},
			)
		}

		composable(Screen.Cull.name) {
			val groups = state.shots.grouped
			val currentGroup = state.currentGroup
			val viewModel = remember(groups) { CullViewModel(groups, currentGroup) }

			CullScreen(viewModel)
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
