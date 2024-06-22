import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import core.src.jni.Core
import core.src.jni.LoadingPipeline
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import screen.cull.CullScreen
import screen.cull.CullViewModel
import screen.import_.ImportScreen
import screen.overview.OverviewScreen

val PrimaryColor = Color(0xFFEDE7F6)
val ContentColor = Color(0xFF7E57C2)

@Composable
fun TopAppBar(title: String) {
	TopAppBar(
		title = { Text(title) }, backgroundColor = PrimaryColor, contentColor = ContentColor
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

	fun backToSelect() {
		model.reset()
		navController.popBackStack(Screen.IMPORT, false)
	}

	Scaffold(topBar = { TopAppBar(title = "A-Shot") }) { padding ->
		NavHost(
			modifier = Modifier.fillMaxSize(),
			navController = navController,
			startDestination = Screen.IMPORT,
		) {
			composable(Screen.IMPORT) {
				ImportScreen(onImported = { dir, collection ->
					model.imported(dir, collection)
					navController.navigate(Screen.OVERVIEW)
				})
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

fun main() {
	Core.load()

	application {
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
//		App(window = window)
			val loader = remember { LoadingPipeline() }
			val good = remember { mutableStateListOf<String>() }
			val bad = remember { mutableStateListOf<String>() }


			LaunchedEffect(loader) {
				launch(Dispatchers.IO) {
					loader.flow().collect { (path, score) ->
						if (score < 0.1f) {
							bad.add(path)
						} else {
							good.add(path)
						}
					}
				}
			}

			Column(modifier = Modifier.fillMaxSize()) {
				Button(onClick = {
					val path = "/home/a/Projects/A-Shot/utils/src/blur_filtering/samples/"
					val result = loader.load(path)
					if (!result) {
						println("Too many clicks")
					}
				}) {
					Text("Load")
				}

				Row {
					Column(Modifier.weight(1f)) {
						Text("Good items")
						LazyColumn {
							items(good) { item ->
								Text(item)
							}
						}
					}
					Column(Modifier.weight(1f)) {
						Text("Bad items")
						LazyColumn {
							items(bad) { item ->
								Text(item)
							}
						}
					}
				}
			}
		}
	}
}
