package com.example.apnaMusic.roomDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SavedArticles::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    abstract fun myDao(): MyDao
}
