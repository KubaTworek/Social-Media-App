package pl.jakubtworek.articles.controller.dto

data class ArticleUpdateRequest(
    val text: String,
    val articleId: Int,
)
