package com.example.authors.exception

data class ErrorResponse(
    val status: Int,
    val message: String?
)