package com.example.magazines.controller


data class MagazineResponse(
    val id: Int,
    val name: String,
    val articles: List<ArticleResponse>,
)