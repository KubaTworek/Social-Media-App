package pl.jakubtworek.common.model

data class ArticleDTO(
    val id: Int,
    val timestamp: String,
    val text: String,
    val authorId: Int
)
