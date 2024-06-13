import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import java.awt.image.BufferedImage
import java.io.File

fun BufferedImage.toImageBitmap(): ImageBitmap = toComposeImageBitmap()

fun getBitmapsFromSelectedSubGroup(
    subGroups: List<List<Pair<ImageBitmap, File>>>,
    selectedSubGroupIndex: Int
): List<ImageBitmap> {
    val selectedSubGroup = subGroups.getOrNull(selectedSubGroupIndex)
    return selectedSubGroup?.map { it.first } ?: emptyList()
}
