package database.dao

import androidx.room.Dao
import androidx.room.Query

@Dao
interface CategoryDao {
	@Query(
		"""
			SELECT COUNT(*)
			FROM shot 
			WHERE 
				folder_id = :folderId AND 
				lucky = TRUE;
		"""
	)
	suspend fun getNiceCount(folderId: Long): Int

	@Query(
		"""
			SELECT COUNT(*)
			FROM shot
			WHERE 
				folder_id = :folderId AND 
				:blurThreshold <= blur_score AND
				lucky IS NULL;
		"""
	)
	suspend fun getUnsortedCount(folderId: Long, blurThreshold: Float): Int

	@Query(
		"""
			SELECT COUNT(*)
			FROM shot
			WHERE
				folder_id = :folderId AND
				(lucky = FALSE OR :blurThreshold > blur_score AND lucky IS NULL);
		"""
	)
	suspend fun getUnluckyCount(folderId: Long, blurThreshold: Float): Int
}