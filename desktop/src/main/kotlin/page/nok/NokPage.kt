package page.nok

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.io.File

@Composable
fun NokPage(
	files: Array<File>,
	onFileClick: (File) -> Unit,
) {
	LazyVerticalGrid(
		modifier = Modifier.fillMaxSize(),
		columns = GridCells.Adaptive(120.dp),
	) {
		items(files.size) {
			val file = files[it]
			NokItem(
				file = file,
				onClick = onFileClick,
			)
		}
	}
}

@Composable
fun NokItem(
	file: File,
	onClick: (File) -> Unit = {}
) {
	Card(
		modifier = Modifier.aspectRatio(1f)
			.clickable { onClick(file) },
	) {
		Text(
			text = "${file.name}!"
		)
	}
}
