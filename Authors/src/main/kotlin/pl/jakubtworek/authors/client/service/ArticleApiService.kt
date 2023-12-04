package pl.jakubtworek.authors.client.service


interface ArticleApiService {
    fun deleteArticlesByAuthorId(authorId: Int)
}