package ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.jetbrains.compose.ui.tooling.preview.Preview

sealed class Screen(val route: String) {
	data object Overview : Screen("overview")
	data object Collection : Screen("collection/{id}")
	data object Normal : Screen("normal/{id}")
	data object Cull : Screen("cull")
}

@Composable
fun AppNavHost(
	modifier: Modifier = Modifier,
	navController: NavHostController,
	startDestination: Screen,

	overview: @Composable () -> Unit,
	folder: @Composable (id: Long) -> Unit,
	normal: @Composable (id: Long) -> Unit,
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
			Screen.Collection.route,
			arguments = listOf(navArgument("id") { type = NavType.LongType })
		) { entry ->
			val id = entry.arguments?.getLong("id") ?: TODO("Return to previous screen")
			folder(id)
		}

		composable(
			Screen.Normal.route,
			arguments = listOf(navArgument("id") { type = NavType.LongType })
		) { entry ->
			val id = entry.arguments?.getLong("id") ?: TODO("Return to previous screen")
			normal(id)
		}

		composable(Screen.Cull.route) {
			cull()
		}
	}
}

@Preview
@Composable
fun AppNavHostPreview() {
	AppNavHost(
		modifier = Modifier.fillMaxSize(),
		navController = rememberNavController(),
		startDestination = Screen.Overview,
		overview = {},
		folder = {},
		normal = {},
		cull = {},
	)
}
