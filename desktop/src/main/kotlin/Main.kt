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
import screen.import_.ImportScreen
import screen.overview.OverviewScreen

object Screen {
	const val IMPORT = "import"
	const val OVERVIEW = "overview"
	const val CULL = "cull"
}

@Composable
@Preview
fun App(window: ComposeWindow) {
	val model = remember { MainModel() }
	val navController = rememberNavController()
	val state by model.stateFlow.collectAsState()

	fun backToSelect() {
		model.reset()
		navController.popBackStack(Screen.IMPORT, false)
	}

	NavHost(
		modifier = Modifier.fillMaxSize(),
		navController = navController,
		startDestination = Screen.IMPORT,
	) {
		composable(Screen.IMPORT) {
			ImportScreen(
				onImported = { dir, collection ->
					model.imported(dir, collection)
					navController.navigate(Screen.OVERVIEW)
				}
			)
		}

		composable(Screen.OVERVIEW) {
			OverviewScreen(
				collection = state.shots,
				onGroupSelected = { group ->
					model.cull(group)
					navController.navigate(Screen.CULL)
				},
				onClose = {
					backToSelect()
				},
			)
		}

		composable(Screen.CULL) {
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
