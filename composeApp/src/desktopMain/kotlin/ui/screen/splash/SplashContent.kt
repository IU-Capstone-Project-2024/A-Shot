package ui.screen.splash

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SplashContent(
	progress: Float,
	message: String,
) {
	Box(
		modifier = Modifier.fillMaxSize()
	) {
		Image(
			modifier = Modifier.fillMaxSize(),
			painter = painterResource("/stubs/gendo.jpg"),
			contentScale = ContentScale.Crop,
			contentDescription = null,
		)

		Box(
			modifier = Modifier
				.align(Alignment.BottomCenter)
		) {
			LinearProgressIndicator(
				modifier = Modifier
					.height(32.dp)
					.fillMaxWidth(),
				progress = progress,
			)

			Text(
				modifier = Modifier
					.align(Alignment.Center),
				text = message,
				color = Color.White,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis,
				fontSize = 14.sp
			)
		}
	}
}

@Preview
@Composable
fun SplashContentPreview() {
	SplashContent(
		progress = 0.75f,
		message = "Starting up",
	)
}
