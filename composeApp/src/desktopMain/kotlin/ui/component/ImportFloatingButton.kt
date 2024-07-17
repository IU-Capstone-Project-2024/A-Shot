package ui.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ImportFloatingButton(
	visible: Boolean,
	onClicked: () -> Unit
) {
	if (visible) {
		ExtendedFloatingActionButton(
			text = { Text("Import") },
			icon = { Icon(Icons.Default.Add, contentDescription = "Import") },
			onClick = onClicked
		)
	}
}

@Preview
@Composable
fun ImportFloatingButtonPreview() {
	ImportFloatingButton(
		visible = true,
		onClicked = {}
	)
}
