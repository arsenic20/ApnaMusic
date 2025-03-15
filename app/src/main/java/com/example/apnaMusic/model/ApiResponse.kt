package com.example.apnaMusic.model

data class ApiResponse<T>(
    val headers: Headers,
    val results: List<T>
)