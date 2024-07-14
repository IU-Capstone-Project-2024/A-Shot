package database.dao

import androidx.room.Dao
import androidx.room.Query
import database.selection.FolderWithCount
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {
	/*@Query("SELECT * FROM folder")
	fun selectAll(): Flow<List<FolderWithpictures>>*/

	@Query(
		"""
			SELECT folder.id, folder.path, COUNT(*) AS count
			FROM folder JOIN shot ON folder.id = shot.folder_id
			GROUP BY folder.id, folder.path
		"""
	)
	fun selectFoldersWithCount(): Flow<List<FolderWithCount>>

	@Query("INSERT OR IGNORE INTO folder (path) VALUES (:path)")
	suspend fun insert(path: String)

	@Query("SELECT id FROM folder WHERE path = :path")
	suspend fun select(path: String): Long?
}
