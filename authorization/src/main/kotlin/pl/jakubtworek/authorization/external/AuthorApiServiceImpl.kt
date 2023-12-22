package pl.jakubtworek.authorization.external

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import pl.jakubtworek.common.client.AuthorClient
import pl.jakubtworek.common.model.AuthorDTO
import pl.jakubtworek.common.model.AuthorRequest

@Service
class AuthorApiServiceImpl(
    private val authorClient: AuthorClient,
    private val objectMapper: ObjectMapper
) : AuthorApiService {
    override fun getAuthorByUsername(username: String): AuthorDTO {
        val response = authorClient.getAuthorByUsername(username)

        return deserializeAuthor(response)
    }

    override fun createAuthor(authorRequest: AuthorRequest) =
        authorClient.createAuthor(authorRequest)

    override fun deleteAuthorById(id: Int) =
        authorClient.deleteAuthorById(id)

    private fun deserializeAuthor(response: ResponseEntity<String>): AuthorDTO =
        objectMapper.readValue(response.body, AuthorDTO::class.java)
}