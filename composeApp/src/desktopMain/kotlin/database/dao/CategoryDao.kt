package database.dao

import database.selection.CategorySizes
import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
	@Query(
		"""
			SELECT
				COUNT(CASE WHEN starred THEN 1 END) AS favourite,
				COUNT(CASE WHEN :blurThreshold <= blur_score THEN 1 END) AS regular,
				COUNT(CASE WHEN :blurThreshold > blur_score THEN 1 END) AS blurry
			FROM
				shot
			WHERE
				folder_id = :folderId
		"""
	)
	fun count(folderId: Long, blurThreshold: Float): Flow<CategorySizes>
}