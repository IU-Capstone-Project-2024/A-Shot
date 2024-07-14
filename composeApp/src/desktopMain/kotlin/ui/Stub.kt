package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.useResource
import database.entity.Folder
import database.selection.FolderWithShots
import database.entity.Shot
import database.selection.FolderWithCount
import database.selection.ShotIdName
import ui.screen.virtual_folder.VirtualFolder
import kotlin.random.Random

fun stubShot(folderId: Long, random: Random = Random(42)): Shot {
	val id = random.nextLong()
	val name = "$id.jpg"
	val blurScore = random.nextFloat()
	val starred = random.nextBoolean()

	return Shot(id, folderId, name, blurScore, ByteArray(0), ByteArray(0), starred)
}

fun stubFolder(random: Random = Random(42)): Folder {
	val id = random.nextLong()
	val name = listOf("My Pictures", "Vacation", "China", "Graduation", "Wedding").random(random)

	return Folder(id, name)
}

fun stubFolderWithCount(random: Random = Random(42)): FolderWithCount {
	val folder = stubFolder(random)
	val count = random.nextInt(1, Int.MAX_VALUE)

	return FolderWithCount(folder, count)
}

fun stubFolderWithShots(random: Random = Random(42)): FolderWithShots {
	val size = random.nextInt(1, 20)
	val folder = stubFolder(random)
	val pictures = List(size) { stubShot(folder.id) }

	return FolderWithShots(folder, pictures)
}

fun stubListOfFolderWithCount(random: Random = Random(42)): List<FolderWithCount> {
	val size = random.nextInt(1, 20)
	val folders = List(size) { stubFolderWithCount(random) }

	return folders
}

fun stubListOfFolderWithShots(random: Random = Random(42)): List<FolderWithShots> {
	val size = random.nextInt(1, 20)
	val folders = List(size) { stubFolderWithShots(random) }

	return folders
}

fun stubShotIdName(random: Random = Random(42)): ShotIdName {
	val id = random.nextLong()
	val name = "$id.jpg"

	return ShotIdName(id, name)
}

fun stubVirtualFolder(random: Random = Random(42)): VirtualFolder {
	val id = random.nextLong()
	val size = random.nextInt(1, 20)
	val pictures = List(size) { random.nextLong() }

	return VirtualFolder(id, pictures)
}

fun stubListOfVirtualFolders(random: Random = Random(42)): List<VirtualFolder> {
	val size = random.nextInt(1, 20)
	val folders = List(size) { stubVirtualFolder(random) }

	return folders
}

fun stubImageBitmap(random: Random = Random(42)): ImageBitmap {
	val image = listOf("stubs/putin.jpg", "stubs/shaurma.jpg").random(random)
	return useResource(image) {
		loadImageBitmap(it)
	}
}

@Preview
@Composable
fun StubImage(modifier: Modifier = Modifier) {
	Image(
		modifier = modifier,
		painter = painterResource("stubs/putin.jpg"),
		contentDescription = "Vladimir Putin"
	)
}
