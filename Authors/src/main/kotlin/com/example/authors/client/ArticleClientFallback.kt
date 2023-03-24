package com.example.authors.client

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.*
import org.springframework.stereotype.Service

@Service
@Qualifier("AuthorClientFallback")
class ArticleClientFallback : ArticleClient {
    override fun getArticlesByAuthor(authorId: Int): ResponseEntity<String> {
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    override fun deleteArticlesByAuthorId(authorId: Int): ResponseEntity<String> {
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
