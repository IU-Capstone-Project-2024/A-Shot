package screen.overview

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import page.tailpond.TailpondPage
import page.groups.GroupsPage
import page.overview.OverviewPage
import shot.ShotCollection

enum class Page {
	Overview,
	Groups,
	Tailpond,
}

@Composable
fun OverviewScreen(
	collection: ShotCollection,
	onGroupSelected: (Int) -> Unit,
	onClose: () -> Unit,
) {
	val navController = rememberNavController()

	NavHost(
		modifier = Modifier.fillMaxSize(),
		navController = navController,
		startDestination = Page.Overview.name
	) {
		composable(Page.Overview.name) {
			OverviewPage(
				onGroupsClick = { navController.navigate(Page.Groups.name) },
				onTailpondClick = { navController.navigate(Page.Tailpond.name) },
			)
		}

		composable(Page.Groups.name) {
			GroupsPage(
				groups = collection.grouped,
				onGroupClick = onGroupSelected,
			)
		}

		composable(Page.Tailpond.name) {
			TailpondPage(
				shots = collection.tailpond,
				onShotClick = {},
			)
		}
	}
}
