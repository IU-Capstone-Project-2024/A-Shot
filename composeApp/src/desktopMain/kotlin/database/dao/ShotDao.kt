package database.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface ShotDao {
	@Query(
		"""
			SELECT TRUE
			FROM shot JOIN folder ON shot.folder_id = folder.id
			WHERE folder.path = :folderPath AND shot.name = :shotName
			LIMIT 1
		"""
	)
	suspend fun contains(folderPath: String, shotName: String): Boolean

	@Query(
		"""
			INSERT INTO shot 
				(folder_id, name, blur_score, embedding, thumbnail, lucky) 
			VALUES 
				(:folderId, :shotName, :blurScore, :embedding, :thumbnail, NULL)
		"""
	)
	suspend fun insert(folderId: Long, shotName: String, blurScore: Float, embedding: ByteArray, thumbnail: ByteArray)
}
