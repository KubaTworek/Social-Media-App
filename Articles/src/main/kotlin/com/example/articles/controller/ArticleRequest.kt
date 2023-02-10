package com.example.articles.controller

data class ArticleRequest(
    val title: String,
    val text: String,
    val magazineId: Int,
    val authorId: Int
)