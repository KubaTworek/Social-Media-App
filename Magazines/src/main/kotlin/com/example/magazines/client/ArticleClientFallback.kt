package com.example.magazines.client

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
@Qualifier("AuthorClientFallback")
class ArticleClientFallback : ArticleClient {
    override fun getArticlesByMagazine(magazineId: Int): ResponseEntity<String> {
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}