package database.dao

import database.selection.CategorySizes
import androidx.room.Dao
import androidx.room.Query

@Dao
interface CategoryDao {
	@Query(
		"""
			SELECT
				COUNT(CASE WHEN lucky THEN 1 END) AS nice,
				COUNT(CASE WHEN :blurThreshold <= blur_score AND lucky is NULL THEN 1 END) AS unsorted,
				COUNT(CASE WHEN lucky = FALSE OR :blurThreshold > blur_score AND lucky IS NULL THEN 1 END) AS unlucky
			FROM
				shot
			WHERE
				folder_id = :folderId
		"""
	)
	suspend fun count(folderId: Long, blurThreshold: Float): CategorySizes
}