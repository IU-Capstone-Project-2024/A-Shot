package ui.screen.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import database.ShotDatabase
import database.selection.CategorySizes
import database.selection.FolderWithCount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.component.AppContainer
import ui.component.AppNavHost
import ui.component.Screen
import ui.screen.home.HomeScreen
import ui.screen.home.categories.FolderScreen
import ui.screen.home.categories.regular.RegularScreen
import ui.screen.viewer.ViewerScreen
import ui.screen.viewer.ViewerViewModel
import ui.screen.virtual_folder.VirtualFolderUiState
import ui.screen.virtual_folder.VirtualFolderViewModel
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

@Composable
fun MainScreen(
	db: ShotDatabase,
	onLoad: (path: String) -> Unit
) {
	val scope = rememberCoroutineScope()
	val navController = rememberNavController()

	val backStackEntry by navController.currentBackStackEntryAsState()
	val backButtonVisible = remember(backStackEntry) { navController.previousBackStackEntry != null }
	val importButtonVisible = remember(backStackEntry) { backStackEntry?.destination?.route == Screen.Home.route }

	AppContainer(
		modifier = Modifier.fillMaxSize(),
		backButtonVisible = backButtonVisible,
		importButtonVisible = importButtonVisible,
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
					onLoad(path)
				} else if (!fileChooser.selectedFiles.isNullOrEmpty()) {
					for (file in fileChooser.selectedFiles) {
						val path = file.absolutePath
						onLoad(path)
					}
				} else {
					// TODO:
				}
			}
		}
	) { paddings ->
		val virtualFoldersViewModel = viewModel<VirtualFolderViewModel> {
			VirtualFolderViewModel { folderId ->
				db.shotDao.embeddings(folderId, 0.05f)
			}
		}

		AppNavHost(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddings),

			navController = navController,
			startDestination = Screen.Home,

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

			folder = { _, folderId ->
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
						navController.navigate("${Screen.Regular.route}/${folderId}") {
							launchSingleTop = true
						}
					},
					onBlurrySelected = {

					}
				)
			},

			regular = { entry, folderId ->
				LaunchedEffect(folderId) {
					virtualFoldersViewModel.load(folderId)
				}

				val state by virtualFoldersViewModel.uiStateFlow.collectAsState()
				RegularScreen(
					state = state,
					thumbnail = { shotId ->
						runCatching { db.shotDao.thumbnail(shotId)?.toComposeImageBitmap() }.getOrNull()
					},
					onVirtualFolderClicked = { folderIndex ->
						navController.navigate("${Screen.Viewer}/${folderIndex}") {
							launchSingleTop = true
							restoreState = true
						}
					}
				)
			},

			viewer = { entry, folderIndex ->
				// TODO: fix
//				val virtualFoldersViewModel = viewModel<VirtualFolderViewModel>(navController.previousBackStackEntry!!)
				val virtualFoldersState by virtualFoldersViewModel.uiStateFlow.collectAsState()

				when (val state = virtualFoldersState) {
					VirtualFolderUiState.Idle, is VirtualFolderUiState.Loading -> {}
					is VirtualFolderUiState.Success -> {
						val viewerViewModel = viewModel {
							ViewerViewModel(
								state.folders,
								folderIndex,
							)
						}

						LaunchedEffect(Unit) {
							viewerViewModel.load()
						}

						val viewerState by viewerViewModel.uiStateFlow.collectAsState()
						ViewerScreen(
							state = viewerState,

							onFolderSelected = viewerViewModel::selectFolder,
							onNextFolder = viewerViewModel::nextFolder,
							onPrevFolder = viewerViewModel::prevFolder,

							onShotSelected = viewerViewModel::selectShot,
							onNextShot = viewerViewModel::nextShot,
							onPrevShot = viewerViewModel::prevShot,

							onBack = { navController.popBackStack() },

							thumbnail = { shotId ->
								db.shotDao.thumbnail(shotId)?.toComposeImageBitmap()
							}
						)
					}
				}
			},
		)
	}
}
