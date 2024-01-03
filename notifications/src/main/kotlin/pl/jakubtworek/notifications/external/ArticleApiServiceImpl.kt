package pl.jakubtworek.notifications.external

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import pl.jakubtworek.common.client.ArticleClient
import pl.jakubtworek.common.exception.AuthorizationApiException
import pl.jakubtworek.common.model.ArticleDTO

@Service
class ArticleApiServiceImpl(
    private val articleClient: ArticleClient,
    private val objectMapper: ObjectMapper
) : ArticleApiService {

    private val logger = LoggerFactory.getLogger(javaClass)

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
        val responseBody: String = requireNotNull(response.body) { "Article API response body is null" }

        require(response.statusCode == HttpStatus.OK) { "Article API request failed with status code: ${response.statusCode}" }

        try {
            val dto = objectMapper.readValue(responseBody, object : TypeReference<T>() {})
            logger.debug("Deserialized response: $dto")
            return dto
        } catch (e: Exception) {
            logger.error("Error deserializing response", e)
            throw AuthorizationApiException("Error deserializing response", e)
        }
    }
}
