import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import core.src.jni.Core
import database.Photo
import database.ShotDao
import database.getDatabaseBuilder
import screen.cull.CullScreen
import screen.cull.CullViewModel
import screen.import_.ImportScreen
import screen.overview.OverviewScreen

val PrimaryColor = Color(0xFFEDE7F6)
val ContentColor = Color(0xFF7E57C2)


@Composable
fun MyTopAppBar(
	title: String,
	navigationIcon: @Composable (() -> Unit)? = null
) {
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
fun App(window: ComposeWindow,shotDao:ShotDao) {

	val model = remember { MainModel() }
	val navController = rememberNavController()
	val state by model.stateFlow.collectAsState()
	val navBackStackEntry by navController.currentBackStackEntryAsState()
	val currentRoute = navBackStackEntry?.destination?.route

	val photos by shotDao.getShots().collectAsState(initial = emptyList())

	fun backToSelect() {
		model.reset()
		navController.popBackStack(Screen.IMPORT, false)
	}

	println("Current num of Enteties in DB: $photos.size")

	Scaffold(topBar = { MyTopAppBar(title = "A-Shot") }) { padding ->
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

	val projectDirectory = System.getProperty("user.dir")
	println(projectDirectory)
	Core.load("$projectDirectory/src/core/cmake-build-debug/libcore.so")

	val dao = getDatabaseBuilder().dao


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
			undecorated = false
		) {
		App(window = window, dao)
		}
	}
}
