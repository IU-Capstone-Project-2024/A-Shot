package database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
	tableName = "shot",
	indices = [
		Index(value = ["name"], unique = true)
	]
)
class Shot(
	@ColumnInfo(name = "id")
	@PrimaryKey(autoGenerate = true)
	val id: Long,

	@ColumnInfo(name = "folder_id")
	val folderId: Long,

	@ColumnInfo(name = "name")
	val name: String,

	@ColumnInfo(name = "blur_score")
	val blurScore: Float,

	@ColumnInfo(name = "embedding", typeAffinity = ColumnInfo.BLOB)
	val embedding: ByteArray,

	@ColumnInfo(name = "thumbnail", typeAffinity = ColumnInfo.BLOB)
	val thumbnail: ByteArray,

	@ColumnInfo(name = "lucky")
	var lucky: Boolean? = null,
)