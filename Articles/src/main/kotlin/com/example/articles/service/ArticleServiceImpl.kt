package com.example.articles.service

import com.example.articles.clients.AuthorClient
import com.example.articles.clients.AuthorizationClient
import com.example.articles.controller.ArticleRequest
import com.example.articles.controller.ArticleResponse
import com.example.articles.exception.UnauthorizedException
import com.example.articles.factories.ArticleFactory
import com.example.articles.model.dto.*
import com.example.articles.repository.ArticleRepository
import com.fasterxml.jackson.databind.ObjectMapper
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
@RequiredArgsConstructor
class ArticleServiceImpl(
    private val articleRepository: ArticleRepository,
    private val articleFactory: ArticleFactory,
    @Qualifier("AuthorClient") private val authorClient: AuthorClient,
    @Qualifier("AuthorizationClient") private val authorizationClient: AuthorizationClient,
    private val objectMapper: ObjectMapper
) : ArticleService {
    override fun findAllOrderByDateDesc(): List<ArticleResponse> =
        articleRepository.findAll(Sort.by(Sort.Direction.DESC, "date"))
            .map { articleFactory.createResponse(it) }

    override fun findById(theId: Int): ArticleResponse =
        articleFactory.createResponse(
            articleRepository.findByIdOrNull(theId)
                ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        )

    override fun findAllByAuthorId(authorId: Int): List<ArticleDTO> =
        articleRepository.findAllByAuthorIdOrderByDate(authorId)
            .stream()
            .map { it.toDTO() }
            .toList()

    override fun findAllByKeyword(theKeyword: String): List<ArticleResponse> =
        articleRepository.findAll(Sort.by(Sort.Direction.DESC, "date"))
            .filter {it.text.contains(theKeyword, ignoreCase = true)
            }
            .map { articleFactory.createResponse(it) }

    override fun save(theArticle: ArticleRequest, jwt: String) {
        val article = articleFactory.createArticle(theArticle, jwt)
        articleRepository.save(article)
    }

    override fun deleteById(theId: Int, jwt: String) {
        val userDetails = deserializeUserDetails(getUserDetailsFromToken(jwt))
        val author = deserializeAuthor(getAuthorByUsername(userDetails.username))
        val article = articleRepository.findByIdOrNull(theId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        if (article.authorId == author.id){
            articleRepository.deleteById(theId)
        } else {
            throw UnauthorizedException("You are not authorized to delete this article!")
        }
    }

    override fun deleteByAuthorId(authorId: Int) =
        articleRepository.deleteAllByAuthorId(authorId)

    private fun getAuthorByUsername(username: String): ResponseEntity<String> =
        authorClient.getAuthorByUsername(username)

    private fun getUserDetailsFromToken(jwt: String): ResponseEntity<String> =
        authorizationClient.getUserDetails(jwt)

    private fun deserializeAuthor(response: ResponseEntity<String>): AuthorDTO =
        objectMapper.readValue(response.body, AuthorDTO::class.java)

    private fun deserializeUserDetails(response: ResponseEntity<String>): UserDetailsDTO =
        objectMapper.readValue(response.body, UserDetailsDTO::class.java)
}