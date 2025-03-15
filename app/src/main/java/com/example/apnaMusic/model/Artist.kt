package com.example.apnaMusic.model

data class Artist(
    val id: String,
    val name: String,
    val image: String,
    val joinDate: String,
    val tracks: List<Tracks>
)