package pl.jakubtworek.authors.external

import org.springframework.stereotype.Service
import pl.jakubtworek.common.client.ArticleClient

@Service
class ArticleApiServiceImpl(
     private val articleClient: ArticleClient
) : ArticleApiService {
    override fun deleteArticlesByAuthorId(authorId: Int) {
        articleClient.deleteArticlesByAuthorId(authorId)
    }
}