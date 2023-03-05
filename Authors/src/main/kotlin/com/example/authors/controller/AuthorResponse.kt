package com.example.authors.controller


data class AuthorResponse(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val username: String,
    val articles: List<String>,
)