package com.example.articles.controller

data class ArticleRequest(
    val title: String,
    val text: String,
    val authorId: Int
)