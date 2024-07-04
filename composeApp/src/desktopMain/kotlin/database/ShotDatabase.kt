package database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import java.io.File

@Database(
    entities = [Photo::class],
    version = 1
)
abstract class ShotDatabase : RoomDatabase() {
    abstract val dao: ShotDao
}

fun getDatabaseBuilder(): ShotDatabase {
    val dbFile = File(System.getProperty("java.io.tmpdir"), "room.db")
    return Room.databaseBuilder<ShotDatabase>(
        name = dbFile.absolutePath
    )
        .setDriver(BundledSQLiteDriver())
        .build()
}