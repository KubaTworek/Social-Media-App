package pl.jakubtworek.notifications.client

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.*
import org.springframework.stereotype.Service

@Service
@Qualifier("ArticleClientFallback")
class ArticleClientFallback : pl.jakubtworek.notifications.client.ArticleClient {
    override fun getArticleById(articleId: Int): ResponseEntity<String> {
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }

    override fun getArticlesByAuthor(authorId: Int): ResponseEntity<String> {
        return ResponseEntity(HttpStatus.NOT_FOUND)
    }
}
