package pl.jakubtworek.notifications.external

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import pl.jakubtworek.common.client.ArticleClient
import pl.jakubtworek.common.model.ArticleDTO
import pl.jakubtworek.notifications.exception.AuthorApiException

@Service
class ArticleApiServiceImpl(
    private val articleClient: ArticleClient,
    private val objectMapper: ObjectMapper
) : ArticleApiService {

    private val logger: Logger = LoggerFactory.getLogger(ArticleApiServiceImpl::class.java)

    override fun getArticleById(articleId: Int): ArticleDTO {
        logger.info("Getting article by ID: $articleId")
        val response = articleClient.getArticleById(articleId)
        return handleApiResponse(response)
    }

    override fun getArticlesByAuthor(authorId: Int): List<ArticleDTO> {
        logger.info("Getting articles by authorId: $authorId")
        val response = articleClient.getArticlesByAuthor(authorId)
        return handleApiResponse(response)
    }

    private inline fun <reified T> handleApiResponse(response: ResponseEntity<String>): T {
        logger.info("Handling API response")
        val responseBody: String? = response.body

        if (response.statusCode != HttpStatus.OK) {
            logger.error("Article API request failed with status code: ${response.statusCode}")
            throw AuthorApiException("Article API request failed with status code: ${response.statusCode}")
        }

        if (responseBody != null) {
            try {
                val dto = objectMapper.readValue(responseBody, object : TypeReference<T>() {})
                logger.debug("Deserialized response: $dto")
                return dto
            } catch (e: Exception) {
                logger.error("Error deserializing response", e)
                throw AuthorApiException("Error deserializing response", e)
            }
        } else {
            logger.error("Article API response body is null")
            throw AuthorApiException("Article API response body is null")
        }
    }
}
