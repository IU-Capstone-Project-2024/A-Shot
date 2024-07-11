package ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AppContainer(
	modifier: Modifier = Modifier,

	backButtonVisible: Boolean,
	importButtonVisible: Boolean,

	onBackClick: () -> Unit,
	onImportClick: () -> Unit,

	content: @Composable (PaddingValues) -> Unit
) {
	Scaffold(
		modifier = modifier,
		topBar = {
			AppTopBar(
				backButtonVisible = backButtonVisible,
				onBackClick = onBackClick,
			)
		},
		floatingActionButton = {
			AnimatedVisibility(visible = importButtonVisible) {
				ImportFloatingButton(onClicked = onImportClick)
			}
		}
	) { padding ->
		content(padding)
	}
}

@Preview
@Composable
fun AppContainerPreview() {
	AppContainer(
		backButtonVisible = true,
		importButtonVisible = true,

		onBackClick = {},
		onImportClick = {},
	) {
	}
}
