package database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Photo(
    @PrimaryKey(autoGenerate = false) val filepath: String,
    val blur_score: Float,
)

