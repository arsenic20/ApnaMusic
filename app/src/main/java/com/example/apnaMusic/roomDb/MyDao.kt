package com.example.apnaMusic.roomDb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: SavedArticles)

    @Delete
    suspend fun delete(entity: SavedArticles)

    @Query("SELECT * FROM my_table")
    suspend fun getAll(): List<SavedArticles>
}
