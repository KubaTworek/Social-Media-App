package com.example.articles.exception

data class ErrorResponse(
    val status: Int,
    val message: String?
)