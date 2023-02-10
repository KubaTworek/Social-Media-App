package com.example.articles.factories

import com.example.articles.clients.AuthorClient
import com.example.articles.clients.MagazineClient
import com.example.articles.controller.ArticleRequest
import com.example.articles.controller.AuthorRequest
import com.example.articles.controller.MagazineRequest
import com.example.articles.model.Article
import com.example.articles.model.Author
import com.example.articles.model.Magazine
import com.fasterxml.jackson.databind.ObjectMapper
import feign.FeignException
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
        val author = deserializeAuthor(getAuthor(request))
        val magazine = deserializeMagazine(getMagazine(request))
        val content = contentFactory.createContent(request.title, request.text)

        return Article(
            0,
            getCurrentDate(),
            System.currentTimeMillis().toString(),
            author,
            magazine,
            content
        )
    }

    private fun getAuthor(request: ArticleRequest): ResponseEntity<String> {
        return authorClient.getAuthor(request.authorId)
    }

    private fun getMagazine(request: ArticleRequest): ResponseEntity<String> {
        return magazineClient.getMagazine(request.magazineId)
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