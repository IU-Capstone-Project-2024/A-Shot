package database.selection

import androidx.room.Embedded
import androidx.room.Relation
import database.entity.Folder
import database.entity.Shot

data class FolderWithShots(
	@Embedded val folder: Folder,
	@Relation(
		parentColumn = "id",
		entityColumn = "folder_id",
	)
	val shots: List<Shot>
)