package com.example.apnaMusic.model

data class Album(
    val id: String,
    val name: String?= null,
    val image: String? =null,
    val releasedate: String? =null,
    val tracks: List<Tracks>
)