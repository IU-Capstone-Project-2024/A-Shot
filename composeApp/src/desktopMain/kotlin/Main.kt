import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.*
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import core.Core
import core.LoadingPipeline
import database.ShotDatabase
import database.selection.CategorySizes
import database.selection.FolderWithCount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.component.AppContainer
import ui.component.AppNavHost
import ui.component.Screen
import ui.screen.cull.CullScreen
import ui.screen.folder.FolderScreen
import ui.screen.normal.NormalScreen
import ui.screen.overview.OverviewScreen
import util.DBSCAN
import util.ShotCluster
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

				OverviewScreen(
					folders = folders,
					onFolderSelected = { folder ->
						navController.navigate("${Screen.Collection}/${folder.id}") {
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
					starredCount = sizes.starred,
					normalCount = sizes.normal,
					blurryCount = sizes.blurry,
					onStarredSelected = {

					},
					onNormalSelected = {
						navController.navigate("${Screen.Normal}/${folderId}") {
							launchSingleTop = true
						}
					},
					onBlurrySelected = {

					}
				)
			},

			normal = { folderId ->
				var clusters by remember { mutableStateOf(emptyList<ShotCluster>()) }

				LaunchedEffect(Unit) {
					launch(Dispatchers.IO) {
						val embeddings = db.shotDao.embeddings(folderId)
						val dbscan = DBSCAN(0.9, 2)
						val clusterIndices = dbscan.cluster(embeddings.map { it.embedding })

						println(clusterIndices)
						var max = clusterIndices.max()
						val result = clusterIndices
							.map {
								if (it == -1) {
									max += 1
									max
								} else {
									it
								}
							}
							.zip(embeddings)
							.groupBy({ it.first }, { it.second.id })
							.map { ShotCluster(it.key, it.value) }
							.sortedBy { it.id }

						clusters = result
					}
				}

				NormalScreen(
					clusters = clusters,
					thumbnail = { shotId ->
						db.shotDao.thumbnail(shotId)?.toComposeImageBitmap()
					}
				)
			},

			cull = {
				CullScreen()
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
