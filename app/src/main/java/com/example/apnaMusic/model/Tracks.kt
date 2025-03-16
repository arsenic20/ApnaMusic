package com.example.apnaMusic.model

data class Tracks(
    val id: String,
    val position: String,
    val name: String? = null,
    val duration: String? = null,
    val audio: String
)