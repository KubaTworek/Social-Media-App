package pl.jakubtworek.authorization.external

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import pl.jakubtworek.common.client.AuthorClient
import pl.jakubtworek.common.exception.AuthorApiException
import pl.jakubtworek.common.model.AuthorDTO
import pl.jakubtworek.common.model.AuthorRequest

@Service
class AuthorApiServiceImpl(
    private val authorClient: AuthorClient,
    private val objectMapper: ObjectMapper
) : AuthorApiService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun getAuthorByUsername(username: String): AuthorDTO {
        logger.info("Getting author by username: $username")
        val response = authorClient.getAuthorByUsername(username)
        return deserializeAuthor(response)
    }

    override fun createAuthor(authorRequest: AuthorRequest) {
        logger.info("Creating author: $authorRequest")
        authorClient.createAuthor(authorRequest)
        logger.info("Author created successfully: $authorRequest")
    }

    override fun deleteAuthorById(id: Int) {
        logger.info("Deleting author by ID: $id")
        authorClient.deleteAuthorById(id)
        logger.info("Author deleted successfully: ID $id")
    }

    private fun deserializeAuthor(response: ResponseEntity<String>): AuthorDTO {
        val responseBody: String = requireNotNull(response.body) { "Author API response body is null" }

        require(response.statusCode == HttpStatus.OK) { "Author API request failed with status code: ${response.statusCode}" }

        try {
            val authorDTO = objectMapper.readValue(responseBody, AuthorDTO::class.java)
            logger.debug("Deserialized author: $authorDTO")
            return authorDTO
        } catch (e: Exception) {
            logger.error("Error deserializing AuthorDTO", e)
            throw AuthorApiException("Error deserializing AuthorDTO", e)
        }
    }
}
