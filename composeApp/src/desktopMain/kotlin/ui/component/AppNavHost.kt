package ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

sealed class Screen(val route: String) {
	data object Overview : Screen("overview")
	data object Collection : Screen("collection")
	data object Normal : Screen("normal")
	data object Cull : Screen("cull")
}

@Composable
fun AppNavHost(
	modifier: Modifier = Modifier,
	navController: NavHostController,
	startDestination: Screen,

	overview: @Composable () -> Unit,
	folder: @Composable (folderId: Long) -> Unit,
	normal: @Composable (folderId: Long) -> Unit,
	cull: @Composable () -> Unit,
) {
	NavHost(
		modifier = modifier,
		navController = navController,
		startDestination = startDestination.route,
	) {
		composable(Screen.Overview.route) {
			overview()
		}

		composable(
			route = "${Screen.Collection.route}/{id}",
			arguments = listOf(navArgument("id") { type = NavType.LongType })
		) { entry ->
			val folderId = entry.arguments?.getLong("id") ?: TODO("Return to previous screen")
			folder(folderId)
		}

		composable(
			route = "${Screen.Normal.route}/{id}",
			arguments = listOf(navArgument("id") { type = NavType.LongType })
		) { entry ->
			val folderId = entry.arguments?.getLong("id") ?: TODO("Return to previous screen")
			normal(folderId)
		}

		composable(Screen.Cull.route) { entry ->
			cull()
		}
	}
}
