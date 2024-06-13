package shot

import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class Shot(
	val file: File
) {

	suspend fun thumbnail() = load().map { image -> image.toComposeImageBitmap() }

	suspend fun load(): Result<BufferedImage> = runCatching {
		withContext(Dispatchers.IO) {
			ImageIO.read(file)
		}
	}
}