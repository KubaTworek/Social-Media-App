package pl.jakubtworek.articles.model.dto

data class ArticleDTO(
    val id: Int,
    val timestamp: String,
    val text: String,
    val authorId: Int
)
