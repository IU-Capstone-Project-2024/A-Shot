import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
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
import util.AsyncImage
import java.awt.Dimension
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.math.absoluteValue

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

fun main(argv: Array<String>) {
	if (argv.size != 1) {
		println("Invalid usage, expected native library path")
		return
	}
	Core.load(argv[0])

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
			val scope = rememberCoroutineScope()
			val loader = remember { LoadingPipeline() }
			val good = remember { mutableStateListOf<String>() }
			val bad = remember { mutableStateListOf<String>() }


			LaunchedEffect(loader) {
				launch(Dispatchers.IO) {
					loader.flow().collect { (path, score) ->
						println(score)
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
					scope.launch {
						val fileChooser = JFileChooser().apply {
							dialogTitle = "Select a file or directory"
							fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
							fileFilter = FileNameExtensionFilter("Directory", "directory")
						}

						if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
							val result = loader.load(fileChooser.selectedFile.absolutePath)
							if (!result) {
								println("Too many clicks")
							}
						}
					}
				}) {
					Text("Load")
				}

				Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
					Column(Modifier.weight(1f).fillMaxHeight()) {
						Text("Good items: ${good.size}")
						LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth()) {
							items(
								good,
								key = { item -> item }
							) { item ->
								AsyncImage(
									modifier = Modifier.aspectRatio(1f),
									file = File(item),
									dimension = Dimension(1024, 1024)
								)
							}
						}
					}
					Column(Modifier.weight(1f).fillMaxHeight()) {
						Text("Bad items: ${bad.size}")
						LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth()) {
							items(
								bad,
								key = { item -> item }
							) { item ->
								AsyncImage(
									modifier = Modifier.aspectRatio(1f),
									file = File(item),
									dimension = Dimension(1024, 1024)
								)
							}
						}
					}
				}
			}
		}
	}
}
