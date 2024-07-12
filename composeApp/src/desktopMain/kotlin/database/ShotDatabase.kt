package database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import database.converter.ShotConverters
import database.dao.CategoryDao
import database.dao.FolderDao
import database.dao.ShotDao
import database.entity.Folder
import database.entity.Shot
import java.io.File

@Database(
	entities = [
		Shot::class,
		Folder::class
	],
	version = 1
)
@TypeConverters(ShotConverters::class)
abstract class ShotDatabase : RoomDatabase() {

	abstract val folderDao: FolderDao
	abstract val categoryDao: CategoryDao
	abstract val shotDao: ShotDao

	companion object {
		val instance by lazy {
			val dbFile = File(System.getProperty("java.io.tmpdir"), "room.db")
			Room.databaseBuilder<ShotDatabase>(dbFile.absolutePath)
				.setDriver(BundledSQLiteDriver())
				.build()
		}
	}
}
