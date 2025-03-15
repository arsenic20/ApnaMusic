package com.example.apnaMusic.model

data class Album(
    val id: String,
    val name: String,
    val image: String,
    val artist_name: String,
    val artist_id: String,
    val tracks: List<Tracks>
)