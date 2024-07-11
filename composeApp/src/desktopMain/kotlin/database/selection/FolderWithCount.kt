package database.selection

import androidx.room.Embedded
import database.entity.Folder

data class FolderWithCount(
	@Embedded val folder: Folder,
	val count: Int,
)
