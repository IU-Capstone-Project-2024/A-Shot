package ui

import database.entity.Folder
import database.selection.FolderWithShots
import database.entity.Shot
import database.selection.FolderWithCount
import database.selection.ShotIdName
import util.ShotCluster
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

fun stubShotIdName(random: Random = Random(42)): ShotIdName {
	val id = random.nextLong()
	val name = "$id.jpg"

	return ShotIdName(id, name)
}

fun stubShotCluster(random: Random = Random(42)): ShotCluster {
	val id = random.nextInt()
	val size = random.nextInt(1, 20)
	val cluster = List(size) { random.nextLong() }

	return ShotCluster(id, cluster)
}

fun stubListOfShotCluster(random: Random = Random(42)): List<ShotCluster> {
	val size = random.nextInt(1, 20)
	val clusters = List(size) { stubShotCluster(random) }

	return clusters
}
