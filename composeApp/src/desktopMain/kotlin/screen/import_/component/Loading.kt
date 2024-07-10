package screen.import_.component

import PrimaryColor
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import style.Roboto

@Composable
fun BoxScope.Loading(
	dirName: String,
	progress: Float,
	statusLeft: Int,
	totalImg: Int,
	onCancel: () -> Unit,
	mainColor: Color = Color(0xFF21005D)
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.align(Alignment.Center)
			.background(PrimaryColor)
			.padding(40.dp, 20.dp),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		Text(
			text = "Importing $dirName",
			style = MaterialTheme.typography.body1.copy(
				letterSpacing = 0.5.sp,
				fontSize = 32.sp,
				fontFamily = Roboto,
				fontWeight = FontWeight.Medium,
				color = mainColor
			),
		)

		Spacer(modifier = Modifier.height(20.dp))

		CircularProgressIndicator(
			progress = progress,
			strokeWidth = 4.dp,
			modifier = Modifier.size(50.dp),
			color = mainColor,
		)

		Spacer(modifier = Modifier.height(20.dp))

		LinearProgressIndicator(
			modifier = Modifier.fillMaxWidth(0.8f),
			progress = progress,
			color = mainColor,
		)

		Spacer(modifier = Modifier.height(20.dp))

		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween,
		) {
			Text(
				text = "Imported $statusLeft",
				style = MaterialTheme.typography.body1.copy(
					fontSize = 16.sp,
					fontFamily = Roboto,
					fontWeight = FontWeight.Medium,
					color = mainColor
				)
			)

			Text(
				//TODO: set how much time left
				text = "Total Images: $totalImg",
				style = MaterialTheme.typography.body1.copy(
					fontSize = 16.sp,
					fontFamily = Roboto,
					fontWeight = FontWeight.Medium,
					color = mainColor
				)
			)
		}

		Spacer(modifier = Modifier.height(20.dp))

		Button(
			colors = ButtonDefaults.buttonColors(
				backgroundColor = PrimaryColor,
			),
			onClick = onCancel,
			shape = RoundedCornerShape(30.dp),
		) {
			Text(
				text = "Cancel",
				style = MaterialTheme.typography.body2.copy(
					fontSize = 16.sp,
					fontFamily = Roboto,
					fontWeight = FontWeight.Medium,
					color = mainColor
				)
			)
		}
	}
}

@Composable
@Preview
private fun LoadingPreview() {
	Container(
		state = Unit
	) {
		Loading(
			dirName = "SomeDir",
			progress = 0.75f,
			statusLeft = 12,
			totalImg = 18,
			onCancel = {}
		)
	}
}
