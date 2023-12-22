package pl.jakubtworek.authors.external


interface ArticleApiService {
    fun deleteArticlesByAuthorId(authorId: Int)
}