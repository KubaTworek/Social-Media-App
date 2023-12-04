package pl.jakubtworek.authorization.client

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody
import pl.jakubtworek.authorization.controller.dto.AuthorRequest

@Service
@Qualifier("AuthorClientFallback")
class AuthorClientFallback : AuthorClient {
    override fun createAuthor(@RequestBody theAuthor: AuthorRequest): ResponseEntity<Void> =
        ResponseEntity(HttpStatus.BAD_REQUEST)

    override fun deleteAuthorById(authorId: Int): ResponseEntity<Void> =
        ResponseEntity(HttpStatus.BAD_REQUEST)

    override fun getAuthorByUsername(username: String): ResponseEntity<String> =
        ResponseEntity(HttpStatus.NOT_FOUND)
}
