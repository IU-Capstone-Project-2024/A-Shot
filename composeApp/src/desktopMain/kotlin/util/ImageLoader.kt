package util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import kotlinx.coroutines.withContext
import java.awt.Dimension
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.min


object ImageLoader {
	private const val MAX_JOBS = 4

	private val cache = File(System.getProperty("java.io.tmpdir"), "images")
	private val semaphore = Semaphore(MAX_JOBS)

	init {
		if (!cache.exists()) {
			cache.mkdirs()
		}
	}

	suspend fun load(file: File, size: Dimension): BufferedImage? =
		withContext(Dispatchers.IO) {
			semaphore.withPermit {
				loadCached(file).getOrElse {
					loadUncached(file, size).getOrNull()
				}
			}
		}

	private fun key(file: File): String {
		// TODO: proper key calculation
		return file.canonicalPath.hashCode().toString()
	}

	private suspend fun loadCached(file: File) =
		withContext(Dispatchers.IO) {
			runCatching {
				val cached = File(cache, "${key(file)}.png")
				ImageIO.read(cached)
			}
		}

	private suspend fun loadUncached(file: File, size: Dimension) =
		withContext(Dispatchers.IO) {
			runCatching {
				val image = downsampleImage(ImageIO.read(file), size)
				runCatching {
					val cached = File(cache, "${key(file)}.png")
					ImageIO.write(image, "png", cached)
				}
				image
			}
		}

	private fun downsampleImage(image: BufferedImage, size: Dimension): BufferedImage {
		val originalWidth: Int = image.getWidth(null)
		val originalHeight: Int = image.getHeight(null)

		// Calculate the scaling factor
		val scaleWidth = size.width.toFloat() / originalWidth
		val scaleHeight = size.height.toFloat() / originalHeight

		// Determine the scaling factor to use based on the smaller scaling factor
		val scaleFactor = min(scaleWidth, scaleHeight)
		if (scaleFactor in 0.75f..1.25f) {
			return image
		}

		// Calculate the new dimensions based on the scaling factor
		val newWidth = (scaleFactor * originalWidth).toInt()
		val newHeight = (scaleFactor * originalHeight).toInt()

		// Create a new BufferedImage with the new dimensions
		val downsampledImage = BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB)

		// Draw the original image onto the new BufferedImage, scaled to the new dimensions
		val graphics = downsampledImage.createGraphics()
		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
		graphics.drawImage(image, 0, 0, newWidth, newHeight, null)
		graphics.dispose()

		return downsampledImage
	}
}
