package com.example.authors.controller


data class AuthorResponse(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val articles: List<String>,
)