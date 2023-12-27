package pl.jakubtworek.common.client

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody
import pl.jakubtworek.common.model.AuthorRequest

@Service
class AuthorClientFallback : AuthorClient {
    override fun getAuthorById(authorId: Int): ResponseEntity<String> =
        ResponseEntity(HttpStatus.NOT_FOUND)

    override fun createAuthor(@RequestBody theAuthor: AuthorRequest): ResponseEntity<Void> =
        ResponseEntity(HttpStatus.BAD_REQUEST)

    override fun deleteAuthorById(authorId: Int): ResponseEntity<Void> =
        ResponseEntity(HttpStatus.BAD_REQUEST)

    override fun getAuthorByUsername(username: String): ResponseEntity<String> =
        ResponseEntity(HttpStatus.NOT_FOUND)

    override fun deleteArticlesByAuthorId(authorId: Int): ResponseEntity<String> {
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
