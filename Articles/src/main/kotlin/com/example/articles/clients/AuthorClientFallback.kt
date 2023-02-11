package com.example.articles.clients

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
@Qualifier("AuthorClientFallback")
class AuthorClientFallback : AuthorClient {
    override fun getAuthor(authorId: Int): ResponseEntity<String> =
        ResponseEntity(HttpStatus.NOT_FOUND)
}
