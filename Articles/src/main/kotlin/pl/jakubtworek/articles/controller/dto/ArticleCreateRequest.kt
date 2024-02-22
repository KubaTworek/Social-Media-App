package pl.jakubtworek.articles.controller.dto

data class ArticleCreateRequest(
    val text: String,
    val articleMotherId: Int?,
)
