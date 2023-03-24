package com.example.articles.service

import com.example.articles.clients.AuthorClient
import com.example.articles.clients.AuthorizationClient
import com.example.articles.controller.dto.ArticleRequest
import com.example.articles.controller.dto.ArticleResponse
import com.example.articles.exception.ArticleNotFoundException
import com.example.articles.exception.UnauthorizedException
import com.example.articles.model.dto.*
import com.example.articles.model.entity.Article
import com.example.articles.repository.ArticleRepository
import com.example.articles.repository.LikeRepository
import com.fasterxml.jackson.databind.ObjectMapper
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class ArticleServiceImpl(
    private val articleRepository: ArticleRepository,
    private val likeRepository: LikeRepository,
    private val authorService: AuthorApiService,
    private val authorizationService: AuthorizationApiService
) : ArticleService {

    override fun findAllOrderByDateDesc(): List<ArticleResponse> =
        articleRepository.findAll(Sort.by(Sort.Direction.DESC, "date"))
            .map { createResponse(it) }

    override fun findAllByKeyword(theKeyword: String): List<ArticleResponse> =
        articleRepository.findAll(Sort.by(Sort.Direction.DESC, "date"))
            .filter {
                it.text.contains(theKeyword, ignoreCase = true)
            }
            .map { createResponse(it) }

    override fun save(theArticle: ArticleRequest, jwt: String) {
        val article = createArticle(theArticle, jwt)
        articleRepository.save(article)
    }

    override fun deleteById(theId: Int, jwt: String) {
        val userDetails = authorizationService.getUserDetails(jwt)
        val article = articleRepository.findByIdOrNull(theId)
            ?: throw ArticleNotFoundException("Article not found")
        if (article.authorId == userDetails.authorId) {
            articleRepository.deleteById(theId)
        } else {
            throw UnauthorizedException("You are not authorized to delete this article!")
        }
    }

    override fun findAllByAuthorId(authorId: Int): List<ArticleDTO> =
        articleRepository.findAllByAuthorIdOrderByDate(authorId)
            .map { mapArticleToDTO(it) }

    override fun deleteByAuthorId(authorId: Int) =
        articleRepository.deleteAllByAuthorId(authorId)

    private fun mapArticleToDTO(article: Article): ArticleDTO =
        ArticleDTO(
            id = article.id,
            date = article.date,
            timestamp = article.timestamp.toString(),
            text = article.text,
            authorId = article.authorId
        )

    private fun createArticle(request: ArticleRequest, jwt: String): Article {
        val userDetails = authorizationService.getUserDetails(jwt)

        return Article(
            id = 0,
            date = getCurrentDate(),
            timestamp = Timestamp(System.currentTimeMillis()),
            text = request.text,
            authorId = userDetails.authorId
        )
    }

    private fun createResponse(theArticle: Article): ArticleResponse {
        val author = authorService.getAuthorById(theArticle.authorId)
        val numOfLikes = likeRepository.countLikesByArticleId(theArticle.id)

        return ArticleResponse(
            id = theArticle.id,
            text = theArticle.text,
            timestamp = theArticle.timestamp,
            author_firstName = author.firstName,
            author_lastName = author.lastName,
            author_username = author.username,
            numOfLikes = numOfLikes,
        )
    }

    private fun getCurrentDate(): String {
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
        return dateFormatter.format(LocalDateTime.now())
    }
}