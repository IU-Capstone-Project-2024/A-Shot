package ui

import database.entity.Folder
import database.selection.FolderWithShots
import database.entity.Shot
import database.selection.FolderWithCount
import kotlin.random.Random

fun stubShot(folderId: Long, random: Random = Random(42)): Shot {
	val id = random.nextLong()
	val name = "$id.jpg"
	val blurScore = random.nextFloat()
	val culled = if (random.nextBoolean()) random.nextBoolean() else null

	return Shot(id, folderId, name, blurScore, ByteArray(0), ByteArray(0), culled)
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
	val shots = List(size) { stubShot(folder.id) }

	return FolderWithShots(folder, shots)
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
