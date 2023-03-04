package com.example.authorization.exception


data class ErrorResponse(
    val status: Int,
    val message: String?
)