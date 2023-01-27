package com.example.articles.controller

data class AuthorResponse(
    val id: Int,
    val fistName: String,
    val lastName: String,
    val articles: List<ArticleResponse>,
)