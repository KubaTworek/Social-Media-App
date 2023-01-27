package com.example.articles.controller.author

import com.example.articles.controller.article.ArticleResponse

data class AuthorResponse(
    val id: Int,
    val fistName: String,
    val lastName: String,
    val articles: List<ArticleResponse>,
)