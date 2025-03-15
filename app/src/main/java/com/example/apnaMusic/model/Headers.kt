package com.example.apnaMusic.model

data class Headers(
    val status: String,
    val code: Int,
    val results_count: Int,
    val error_message: String
)