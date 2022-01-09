package com.shortcut.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shortcut.data.local.db.dao.ComicDAO
import com.shortcut.data.local.entity.ComicEntity

@Database(
    entities = [ComicEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    companion object{
        const val DB_NAME = "xkcd_db"
    }

    abstract fun getComicDao(): ComicDAO
}