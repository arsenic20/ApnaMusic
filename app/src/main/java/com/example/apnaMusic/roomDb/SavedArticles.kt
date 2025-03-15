package com.example.apnaMusic.roomDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_table")
data class SavedArticles(
    @PrimaryKey val id: Int = 0,
     val author: String,
     val description: String,
     val urlToImage: String,
     val url: String
)
