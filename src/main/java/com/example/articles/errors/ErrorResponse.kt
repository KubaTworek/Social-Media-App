package com.example.articles.errors

data class ErrorResponse(
    val status: Int,
    val message: String?
)