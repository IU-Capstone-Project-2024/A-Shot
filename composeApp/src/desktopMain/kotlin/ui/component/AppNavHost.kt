package ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

sealed class Screen(val route: String) {
	data object Home : Screen("Home")
	data object Collection : Screen("Collection")
	data object Regular : Screen("Regular")
	data object Viewer : Screen("Viewer")
}

@Composable
fun AppNavHost(
	modifier: Modifier = Modifier,
	navController: NavHostController,
	startDestination: Screen,

	overview: @Composable (entry: NavBackStackEntry) -> Unit,
	folder: @Composable (entry: NavBackStackEntry, folderId: Long) -> Unit,
	regular: @Composable (entry: NavBackStackEntry, folderId: Long) -> Unit,
	viewer: @Composable (entry: NavBackStackEntry, folderIndex: Int) -> Unit,
) {
	NavHost(
		modifier = modifier,
		navController = navController,
		startDestination = "$startDestination",
	) {
		composable("${Screen.Home}") { entry ->
			overview(entry)
		}

		composable(
			route = "${Screen.Collection}/{id}",
			arguments = listOf(navArgument("id") { type = NavType.LongType })
		) { entry ->
			val folderId = entry.arguments?.getLong("id") ?: TODO("Return to previous screen")
			folder(entry, folderId)
		}

		composable(
			route = "${Screen.Regular}/{id}",
			arguments = listOf(navArgument("id") { type = NavType.LongType })
		) { entry ->
			val folderId = entry.arguments?.getLong("id") ?: TODO("Return to previous screen")
			regular(entry, folderId)
		}

		composable(
			"${Screen.Viewer}/{folderIndex}",
			arguments = listOf(navArgument("folderIndex") { type = NavType.IntType })
		) { entry ->
			val folderIndex = entry.arguments?.getInt("folderIndex") ?: 0
			viewer(entry, folderIndex)
		}
	}
}
