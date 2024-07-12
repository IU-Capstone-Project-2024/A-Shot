@file:OptIn(ExperimentalMaterial3Api::class)

package ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun AppTopBar(
	backButtonVisible: Boolean,
	onBackClick: () -> Unit,
) {
	TopAppBar(
		title = { Text("A-Shot") },
		navigationIcon = {
			AnimatedVisibility(visible = backButtonVisible) {
				IconButton(onClick = onBackClick) {
					Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Back")
				}
			}
		}
	)
}

@Preview
@Composable
fun AppTopBarPreview() {
	AppTopBar(
		backButtonVisible = true,
		onBackClick = {},
	)
}
