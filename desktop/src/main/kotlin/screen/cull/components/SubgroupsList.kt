package screen.cull.components
import shot.ShotGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO



fun BufferedImage.toImageBitmap(): ImageBitmap = toComposeImageBitmap()

fun fileToImageBitmap(file: File, cache: MutableMap<File, ImageBitmap?>): ImageBitmap? {
    return cache.getOrPut(file) {
        if (file.name.endsWith(".png", ignoreCase = true)) {
            val bufferedImage = ImageIO.read(file)
            bufferedImage?.toImageBitmap()
        } else {
            null
        }
    }
}


@Composable
fun SubgroupsList(
	modifier: Modifier = Modifier,
	current: Int,
	subgroups: List<ShotGroup>,
	onSubgroupSelected: (Int) -> Unit,
    cache: MutableMap<File, ImageBitmap?>
) {
	LazyRow(modifier = modifier) {
		itemsIndexed(subgroups) { index, subgroup ->
			SubgroupItem(
				subgroup = subgroup,
				onClick = { onSubgroupSelected(index) },
                cache = cache
			)
		}
	}
}

@Composable
fun SubgroupItem(
	subgroup: ShotGroup,
	onClick: () -> Unit,
    cache: MutableMap<File, ImageBitmap?>
) {
	Box(
		modifier = Modifier
			.aspectRatio(1.0f)
			.clickable(onClick = onClick),
	) {
		val imageBitmap = fileToImageBitmap(subgroup.shots[0].file, cache)
		if (imageBitmap != null) {
			Image(
				bitmap = imageBitmap,
				contentDescription = null,
				modifier = Modifier.fillMaxSize()
			)
		}
	}
}
