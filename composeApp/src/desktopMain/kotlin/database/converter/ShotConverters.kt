package database.converter

import androidx.room.TypeConverter
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

class ShotConverters {
	@TypeConverter
	fun fromBufferedImageToByteArray(bufferedImage: BufferedImage?): ByteArray? {
		if (bufferedImage == null) {
			return null
		}

		val byteArrayOutputStream = ByteArrayOutputStream()
		val iterator = ImageIO.getImageWritersByFormatName("jpg")
		val imageWriter = iterator.next()

		imageWriter.output = ImageIO.createImageOutputStream(byteArrayOutputStream)
		imageWriter.write(bufferedImage)
		imageWriter.dispose()

		return byteArrayOutputStream.toByteArray()
	}

	@TypeConverter
	fun fromByteArrayToBufferedImage(byteArray: ByteArray?): BufferedImage? {
		if (byteArray == null) {
			return null
		}

		val inputStream = ByteArrayInputStream(byteArray)
		return ImageIO.read(inputStream)
	}
}