package pl.jakubtworek.authors.external

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import pl.jakubtworek.common.client.ArticleClient

@Service
class ArticleApiServiceImpl(
    private val articleClient: ArticleClient
) : ArticleApiService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun deleteArticlesByAuthorId(authorId: Int) {
        logger.info("Deleting articles by author ID: $authorId")
        articleClient.deleteArticlesByAuthorId(authorId)
        logger.info("Articles deleted successfully for author ID: $authorId")
    }
}
