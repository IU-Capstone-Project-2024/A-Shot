package screen.import_.component

import PrimaryColor
import style.Roboto
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

private fun selectDirectory(title: String): File? {
	val fileChooser = JFileChooser().apply {
		dialogTitle = title
		fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
		fileFilter = FileNameExtensionFilter("Directory", "directory")
	}

	val result = fileChooser.showOpenDialog(null)
	if (result == JFileChooser.APPROVE_OPTION) {
		return fileChooser.selectedFile
	}
	return null
}

@Composable
fun BoxScope.SelectDir(onDirSelected: (File) -> Unit) {
	Button(
		colors = ButtonDefaults.buttonColors(
			backgroundColor = PrimaryColor,
			contentColor = Color.Transparent
		),
		modifier = Modifier.align(Alignment.Center),
		onClick = {
			val dir = selectDirectory("Select Directory")
			if (dir != null) {
				onDirSelected(dir)
			}
		}
	) {
		Column(
			modifier = Modifier.fillMaxSize(),
			verticalArrangement = Arrangement.Center,
			horizontalAlignment = Alignment.CenterHorizontally
		) {
			Text(
				text = "Import directory",
				style = MaterialTheme.typography.body1.copy(
					fontSize = 32.sp,
					fontFamily = Roboto,
					fontWeight = FontWeight.Medium,
					color = Color(0xFF21005D)
				),
			)

			Spacer(modifier = Modifier.height(20.dp))

			Image(
				painter = painterResource("drawable/download_icon_v1.0.png"),
				contentDescription = "Icon",
			)
		}
	}
}

@Composable
@Preview
private fun SelectDirPreview() {
	Container(
		state = Unit
	) {
		SelectDir {}
	}
}
