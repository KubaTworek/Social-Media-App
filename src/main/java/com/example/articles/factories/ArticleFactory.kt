package com.example.articles.factories

import com.example.articles.controller.article.ArticleRequest
import com.example.articles.entity.Article
import com.example.articles.entity.Author
import com.example.articles.entity.Magazine

import com.example.articles.repository.AuthorRepository
import com.example.articles.repository.MagazineRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Component
class ArticleFactory(
    private val authorRepository: AuthorRepository,
    private val magazineRepository: MagazineRepository,
    private val authorFactory: AuthorFactory,
    private val magazineFactory: MagazineFactory,
    private val contentFactory: ContentFactory,
) {
    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")

    fun createArticle(request: ArticleRequest): Article {
        val author = getOrCreateAuthor(request)
        val magazine = getOrCreateMagazine(request)
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

    private fun getOrCreateAuthor(request: ArticleRequest): Author {
        return authorRepository.findByFirstNameAndLastName(request.author_firstName, request.author_lastName)
            .orElse(authorFactory.createAuthor(request.author_firstName, request.author_lastName))
    }

    private fun getOrCreateMagazine(request: ArticleRequest): Magazine {
        return magazineRepository.findByName(request.magazine)
            .orElse(magazineFactory.createMagazine(request.magazine))
    }

    private fun getCurrentDate(): String {
        return dateFormatter.format(LocalDateTime.now())
    }
}