package com.example.apnaMusic.model

data class Artist(
    val id: String,
    val name: String? = null,
    val image: String? = null,
    val joindate: String? = null,
    val tracks: List<Tracks>? = null
)