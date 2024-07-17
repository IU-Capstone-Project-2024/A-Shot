package database.dao

import androidx.room.Dao
import androidx.room.Query
import database.selection.ShotIdEmbedding
import java.awt.image.BufferedImage

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
				(folder_id, name, blur_score, embedding, thumbnail, starred) 
			VALUES 
				(:folderId, :shotName, :blurScore, :embedding, :thumbnail, FALSE)
		"""
	)
	suspend fun insert(folderId: Long, shotName: String, blurScore: Float, embedding: ByteArray, thumbnail: ByteArray)

	@Query("SELECT thumbnail FROM shot WHERE id = :shotId")
	suspend fun thumbnail(shotId: Long): BufferedImage?

	@Query("SELECT id, embedding FROM shot WHERE folder_id = :folderId")
	suspend fun embeddings(folderId: Long): List<ShotIdEmbedding>

	@Query("SELECT id, embedding FROM shot WHERE folder_id = :folderId AND blur_score >= :blurThreshold")
	suspend fun embeddings(folderId: Long, blurThreshold: Float): List<ShotIdEmbedding>
}
