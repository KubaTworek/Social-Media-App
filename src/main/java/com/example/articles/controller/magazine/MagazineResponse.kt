package com.example.articles.controller.magazine

import com.example.articles.controller.article.ArticleResponse

data class MagazineResponse(
    val id: Int,
    val name: String,
    val articles: List<ArticleResponse>,
)