import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState

val PrimaryColor = Color(0xFFEDE7F6)
val ContentColor = Color(0xFF7E57C2)


@Composable
fun MyTopAppBar(
	title: String,
	navigationIcon: @Composable (() -> Unit)? = null
) {
	TopAppBar(
		title = { Text(title) },
		backgroundColor = PrimaryColor,
		contentColor = ContentColor,
		navigationIcon = navigationIcon
	)
}

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
	val navBackStackEntry by navController.currentBackStackEntryAsState()
	val currentRoute = navBackStackEntry?.destination?.route
	fun backToSelect() {
		model.reset()
		navController.popBackStack(Screen.IMPORT, false)
	}

	Scaffold(
		topBar = {
			MyTopAppBar(
				title = "A-Shot",
				navigationIcon = when (currentRoute) {
					Screen.IMPORT -> {
						{
							IconButton(onClick = { /*for later development, maybe not needed*/ }) {
								Icon(
									painter = painterResource("icons/custom_icon.png"),
									contentDescription = null
								)
							}
						}
					}
					else -> {
						if (navController.previousBackStackEntry != null) {
							{
								IconButton(onClick = { navController.popBackStack() }) {
									Icon(
										imageVector = Icons.AutoMirrored.Default.ArrowBack,
										contentDescription = null
									)
								}
							}
						} else {
							null
						}
					}
				}
			)
		}
	) { padding ->
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
		icon = painterResource("icons/icon.png"),
		undecorated = false //Should be true for custom Title Bar
	) {
		App(window = window)
	}
}
