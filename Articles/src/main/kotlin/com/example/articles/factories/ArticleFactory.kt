package com.example.articles.factories

import com.example.articles.clients.AuthorClient
import com.example.articles.clients.MagazineClient
import com.example.articles.controller.ArticleRequest
import com.example.articles.controller.ArticleResponse
import com.example.articles.model.Article
import com.example.articles.model.Author
import com.example.articles.model.Magazine
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Component
class ArticleFactory(
    private val authorClient: AuthorClient,
    private val magazineClient: MagazineClient,
    private val contentFactory: ContentFactory,
) {
    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")

    fun createArticle(request: ArticleRequest): Article {
        val content = contentFactory.createContent(request.title, request.text)

        return Article(
            0,
            getCurrentDate(),
            System.currentTimeMillis().toString(),
            request.authorId,
            request.magazineId,
            content
        )
    }

    fun createResponse(theArticle: Article) : ArticleResponse{
        val author = deserializeAuthor(getAuthor(theArticle.authorId))
        val magazine = deserializeMagazine(getMagazine(theArticle.magazineId))

        return ArticleResponse(
            0,
            theArticle.content.title,
            theArticle.content.text,
            magazine.name,
            author.firstName,
            author.lastName
        )
    }

    private fun getAuthor(authorId: Int): ResponseEntity<String> {
        return authorClient.getAuthor(authorId)
    }

    private fun getMagazine(magazineId: Int): ResponseEntity<String> {
        return magazineClient.getMagazine(magazineId)
    }

    private fun getCurrentDate(): String {
        return dateFormatter.format(LocalDateTime.now())
    }

    private fun deserializeAuthor(response: ResponseEntity<String>): Author {
        val objectMapper = ObjectMapper()
        return objectMapper.readValue(response.body, Author::class.java)
    }

    private fun deserializeMagazine(response: ResponseEntity<String>): Magazine {
        val objectMapper = ObjectMapper()
        return objectMapper.readValue(response.body, Magazine::class.java)
    }
}