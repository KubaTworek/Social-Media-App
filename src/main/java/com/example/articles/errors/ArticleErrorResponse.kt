package com.example.articles.errors

data class ArticleErrorResponse(
    val status: Int,
    val message: String?
)