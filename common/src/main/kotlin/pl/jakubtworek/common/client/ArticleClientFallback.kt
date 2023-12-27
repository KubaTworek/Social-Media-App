package pl.jakubtworek.common.client

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class ArticleClientFallback : ArticleClient {
    override fun deleteArticlesByAuthorId(authorId: Int): ResponseEntity<String> {
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }

    override fun getArticleById(articleId: Int): ResponseEntity<String> {
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    override fun getArticlesByAuthor(authorId: Int): ResponseEntity<String> {
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }
}