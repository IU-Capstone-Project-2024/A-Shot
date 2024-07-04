package database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface ShotDao {

    @Upsert
    suspend fun upsert(shot: Photo)

    @Delete
    suspend fun deleteImage(shot: Photo)

    @Query("SELECT * FROM Photo")
    fun getShots(): Flow<List<Photo>>

    @Query("SELECT * FROM Photo ORDER BY filepath ASC")
    fun getShotsOrderedByPath(): Flow<List<Photo>>
}