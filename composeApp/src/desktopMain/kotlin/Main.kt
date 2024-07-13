import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import core.Core
import core.LoadingPipeline
import database.ShotDatabase
import database.selection.CategorySizes
import database.selection.FolderWithCount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import logic.CullViewModel
import logic.NormalUiState
import logic.NormalViewModel
import ui.component.AppContainer
import ui.component.AppNavHost
import ui.component.Screen
import ui.screen.cull.CullScreen
import ui.screen.folder.FolderScreen
import ui.screen.regular.NormalScreen
import ui.screen.home.HomeScreen
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

@Composable
fun App() {
	val scope = rememberCoroutineScope()

	val db = remember { ShotDatabase.instance }
	val loadingPipeline = remember(db) { LoadingPipeline(db.folderDao, db.shotDao) }
	val navController = rememberNavController()

	val backStackEntry by navController.currentBackStackEntryAsState()
	val backButtonVisible = remember(backStackEntry) { navController.previousBackStackEntry != null }

	AppContainer(
		modifier = Modifier.fillMaxSize(),
		backButtonVisible = backButtonVisible,
		importButtonVisible = true,
		onBackClick = {
			navController.popBackStack()
		},
		onImportClick = {
			scope.launch(Dispatchers.IO) {
				val fileChooser = JFileChooser().apply {
					dialogTitle = "Choose an image or a directory"
					fileSelectionMode = JFileChooser.FILES_AND_DIRECTORIES
					fileFilter = FileNameExtensionFilter("Directory or image", "*")
				}

				val result = fileChooser.showOpenDialog(null)
				if (result != JFileChooser.APPROVE_OPTION) {
					return@launch
				}

				if (fileChooser.selectedFile != null) {
					val path = fileChooser.selectedFile.absolutePath
					loadingPipeline.load(path)
				} else if (!fileChooser.selectedFiles.isNullOrEmpty()) {
					for (file in fileChooser.selectedFiles) {
						val path = file.absolutePath
						loadingPipeline.load(path)
					}
				} else {
					// TODO:
				}
			}
		}
	) { paddings ->
		val normalViewModel = viewModel { NormalViewModel(db.shotDao) }

		AppNavHost(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddings),

			navController = navController,
			startDestination = Screen.Overview,

			overview = {
				var folders by remember { mutableStateOf(emptyList<FolderWithCount>()) }

				LaunchedEffect(Unit) {
					launch(Dispatchers.IO) {
						db.folderDao.selectFoldersWithCount().collect { value ->
							folders = value
						}
					}
				}

				HomeScreen(
					folders = folders,
					onFolderSelected = { folder ->
						navController.navigate("${Screen.Collection.route}/${folder.id}") {
							launchSingleTop = true
						}
					}
				)
			},

			folder = { folderId ->
				var sizes by remember { mutableStateOf(CategorySizes(0, 0, 0)) }

				LaunchedEffect(Unit) {
					launch(Dispatchers.IO) {
						db.categoryDao.count(folderId, 0.05f).collect { value ->
							sizes = value
						}
					}
				}

				FolderScreen(
					favouriteCount = sizes.favourite,
					regularCount = sizes.regular,
					blurryCount = sizes.blurry,
					onStarredSelected = {

					},
					onNormalSelected = {
						navController.navigate("${Screen.Normal.route}/${folderId}") {
							launchSingleTop = true
						}
					},
					onBlurrySelected = {

					}
				)
			},

			normal = { folderId ->
				LaunchedEffect(folderId) {
					normalViewModel.load(folderId)
				}

				NormalScreen(
					normalViewModel = normalViewModel,
					onClusterClicked = { clusterIndex ->
						normalViewModel.setCurrentCluster(clusterIndex)
						navController.navigate(Screen.Cull.route)
					}
				)
			},

			cull = {
				val state = normalViewModel.state.value as? NormalUiState.Success ?: TODO("AAAAAAAAAAAA")
				val cullViewModel = viewModel {
					CullViewModel(
						shotDao = db.shotDao,
						initialCluster = state.currentCluster,
						clusters = state.clusters,
					)
				}

				/*LaunchedEffect(clusterIndex) {
					normalViewModel
				}*/

				CullScreen(cullViewModel = cullViewModel)
			},
		)
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
			undecorated = false
		) {
			App()
		}
	}
}
