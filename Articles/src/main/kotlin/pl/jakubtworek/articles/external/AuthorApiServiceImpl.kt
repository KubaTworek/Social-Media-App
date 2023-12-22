package pl.jakubtworek.articles.external

import pl.jakubtworek.common.client.AuthorClient
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import pl.jakubtworek.common.model.AuthorDTO

@Service
class AuthorApiServiceImpl(
    private val authorClient: AuthorClient,
    private val objectMapper: ObjectMapper
) : AuthorApiService {

    override fun getAuthorById(authorId: Int): AuthorDTO {
        val response = authorClient.getAuthorById(authorId)
        return deserializeAuthor(response)
    }

    private fun deserializeAuthor(response: ResponseEntity<String>): AuthorDTO =
        objectMapper.readValue(response.body, AuthorDTO::class.java)
}
