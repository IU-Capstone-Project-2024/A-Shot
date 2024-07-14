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
	data object Overview : Screen("overview")
	data object Collection : Screen("collection")
	data object Regular : Screen("regular")
	data object Viewer : Screen("viewer")

	override fun toString(): String {
		return route
	}
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
		composable("${Screen.Overview}") { entry ->
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
