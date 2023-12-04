package pl.jakubtworek.authors.client.service

import pl.jakubtworek.authors.client.ArticleClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class ArticleApiServiceImpl(
    @Qualifier("ArticleClient") private val articleClient: ArticleClient
) : ArticleApiService {
    override fun deleteArticlesByAuthorId(authorId: Int) {
        articleClient.deleteArticlesByAuthorId(authorId)
    }
}