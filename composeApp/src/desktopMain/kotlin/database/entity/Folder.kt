package database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "folder")
data class Folder(
	@ColumnInfo(name = "id")
	@PrimaryKey(autoGenerate = true)
	val id: Long,

	@ColumnInfo(name = "path")
	val path: String,
)