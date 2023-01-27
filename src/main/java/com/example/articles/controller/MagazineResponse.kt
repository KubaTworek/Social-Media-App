package com.example.articles.controller

data class MagazineResponse(
    val id: Int,
    val name: String,
    val articles: List<ArticleResponse>,
)