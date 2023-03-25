package com.example.authorization.client

import com.example.authorization.controller.dto.AuthorRequest
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody

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
