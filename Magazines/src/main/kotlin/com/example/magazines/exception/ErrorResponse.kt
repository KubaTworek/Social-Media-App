package com.example.magazines.exception

data class ErrorResponse(
    val status: Int,
    val message: String?
)