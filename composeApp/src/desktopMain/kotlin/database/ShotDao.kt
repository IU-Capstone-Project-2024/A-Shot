package database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
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

    @Query("SELECT * FROM Photo WHERE filepath = :filepath")
    suspend fun getByPath(filepath: String): Photo?

    @Update
    suspend fun updatePhoto(photo: Photo)
}