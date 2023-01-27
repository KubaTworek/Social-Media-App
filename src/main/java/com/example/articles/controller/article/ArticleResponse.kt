package com.example.articles.controller.article

data class ArticleResponse(
    val id: Int,
    val title: String,
    val text: String,
    val magazine: String,
    val author_firstName: String,
    val author_lastName: String
)