package com.example.articles.factories

import com.example.articles.controller.ArticleRequest
import com.example.articles.entity.Article
import com.example.articles.entity.ArticleContent
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
    fun createArticle(theArticle: ArticleRequest): Article {
        val author = authorRepository.findByFirstNameAndLastName(theArticle.author_firstName, theArticle.author_lastName)
                .orElse(authorFactory.createAuthor(theArticle.author_firstName, theArticle.author_lastName))
        val magazine = magazineRepository.findByName(theArticle.magazine)
            .orElse(magazineFactory.createMagazine(theArticle.magazine))
        val content = contentFactory.createContent(theArticle.title, theArticle.text)

        return Article(
            0,
            getCurrentDate(),
            System.currentTimeMillis().toString(),
            author,
            magazine,
            content
        )
    }

    private fun getCurrentDate(): String {
        val dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
        val now = LocalDateTime.now()

        return dtf.format(now)
    }
}