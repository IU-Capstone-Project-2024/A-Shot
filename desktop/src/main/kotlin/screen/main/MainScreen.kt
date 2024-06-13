package screen.main

import PhotoGroup
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import page.nok.NokPage
import page.ok.OkPage
import page.oknok.OkNokPage
import java.io.File

sealed class Page(val route: String) {
	data object OkNok : Page("oknok")
	data object Ok : Page("ok")
	data object Nok : Page("nok")
}

@Composable
fun MainScreen(dir: File) {
	val viewModel = remember { MainViewModel(dir) }
	val state by viewModel.stateFlow.collectAsState()

	LaunchedEffect(key1 = viewModel) {
		viewModel.loadImages()
	}

	Box(
		modifier = Modifier.fillMaxSize(),
	) {
		when (val currentState = state) {
			MainState.Failure -> {
				Text(text = "FAILURE!")
			}

			MainState.Loading -> {
				CircularProgressIndicator(
					modifier = Modifier.align(Alignment.Center),
				)
			}

			is MainState.Success -> {
				MainContent(currentState.ok, currentState.nok)
			}
		}
	}
}

@Composable
fun MainContent(ok: Array<PhotoGroup>, nok: Array<File>) {
	val navController = rememberNavController()
	NavHost(
		modifier = Modifier.fillMaxSize(),
		navController = navController,
		startDestination = Page.OkNok.route,
	) {
		composable(Page.OkNok.route) {
			OkNokPage(
				onOkClick = { navController.navigate(Page.Ok.route) },
				onNokClick = { navController.navigate(Page.Nok.route) },
			)
		}

		composable(Page.Ok.route) {
			OkPage(
				groups = ok,
				onGroupClick = {}
			)
		}

		composable(Page.Nok.route) {
			NokPage(
				files = nok,
				onFileClick = {}
			)
		}
	}
}
