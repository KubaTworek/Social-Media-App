package com.example.articles.service

import com.example.articles.client.service.AuthorApiService
import com.example.articles.client.service.AuthorizationApiService
import com.example.articles.controller.dto.ArticleRequest
import com.example.articles.exception.ArticleNotFoundException
import com.example.articles.exception.UnauthorizedException
import com.example.articles.model.dto.AuthorDTO
import com.example.articles.model.dto.UserDetailsDTO
import com.example.articles.model.entity.Article
import com.example.articles.repository.ArticleRepository
import com.example.articles.repository.LikeRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.springframework.data.domain.Sort
import java.sql.Timestamp
import java.util.*

class ArticleServiceTest {
    @Mock
    private lateinit var articleRepository: ArticleRepository

    @Mock
    private lateinit var likeRepository: LikeRepository

    @Mock
    private lateinit var authorService: AuthorApiService

    @Mock
    private lateinit var authorizationService: AuthorizationApiService

    private lateinit var articleService: ArticleService

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        articleService = ArticleServiceImpl(
            articleRepository,
            likeRepository,
            authorService,
            authorizationService
        )
    }

    @Test
    fun `findAllOrderByDateDesc should returned all articles`() {
        // Given
        val author = AuthorDTO(1, "firstName", "lastName", "Username")
        val articles = listOf(
            Article(1, "2023-06-27", Timestamp(System.currentTimeMillis()), "Article 1", 1),
            Article(2, "2023-06-26", Timestamp(System.currentTimeMillis()), "Article 2", 2),
            Article(3, "2023-06-25", Timestamp(System.currentTimeMillis()), "Article 3", 1)
        )
        `when`(articleRepository.findAll(any<Sort>())).thenReturn(articles)
        `when`(authorService.getAuthorById(any(Int::class.java))).thenReturn(author)
        `when`(likeRepository.countLikesByArticleId(any(Int::class.java))).thenReturn(5)

        // When
        val result = articleService.findAllOrderByCreatedTimeDesc()

        // Then
        assertEquals(3, result.size)
        assertEquals("Article 1", result[0].text)
        assertEquals("Article 2", result[1].text)
        assertEquals("Article 3", result[2].text)
    }

    @Test
    fun `findAllByKeyword should return articles containing the keyword`() {
        // Given
        val author = AuthorDTO(1, "firstName", "lastName", "Username")
        val articles = listOf(
            Article(1, "2023-06-25", Timestamp(System.currentTimeMillis()), "Article 1", 1),
            Article(2, "2023-06-26", Timestamp(System.currentTimeMillis()), "Keyword Article", 2),
            Article(3, "2023-06-27", Timestamp(System.currentTimeMillis()), "Another Article", 1)
        )
        `when`(articleRepository.findAll(any<Sort>())).thenReturn(articles)
        `when`(authorService.getAuthorById(any(Int::class.java))).thenReturn(author)
        `when`(likeRepository.countLikesByArticleId(any(Int::class.java))).thenReturn(5)

        // When
        val result = articleService.findAllByKeyword("keyword")

        // Then
        assertEquals(1, result.size)
        assertEquals("Keyword Article", result[0].text)
    }

    @Test
    fun `findById should return the article with the given ID`() {
        // Given
        val articleId = 1
        val article = Article(1, "2023-06-25", Timestamp(System.currentTimeMillis()), "Article 1", 1)
        `when`(articleRepository.findById(articleId)).thenReturn(Optional.of(article))

        // When
        val result = articleService.findById(articleId)

        // Then
        assertEquals(articleId, result.id)
        assertEquals("Article 1", result.text)
    }

    @Test
    fun `findById should throw ArticleNotFoundException when article is not found`() {
        // Given
        val articleId = 1
        `when`(articleRepository.findById(articleId)).thenReturn(Optional.ofNullable(null))

        // When & Then
        assertThrows(ArticleNotFoundException::class.java) {
            articleService.findById(articleId)
        }
    }

    @Test
    fun `save should create and save a new article`() {
        // Given
        val articleRequest = ArticleRequest("Title", "Text")
        val jwt = "dummy-jwt"
        val userDetails = UserDetailsDTO(1, "FirstName", "LastName", "Username", "Role")
        val expectedArticle = Article(1, "2023-06-25", Timestamp(System.currentTimeMillis()), "Article 1", 1)
        `when`(authorizationService.getUserDetails(jwt)).thenReturn(userDetails)
        `when`(articleRepository.save(any())).thenReturn(expectedArticle)

        // When
        articleService.save(articleRequest, jwt)

        // Then
        verify(articleRepository).save(any())
    }

    @Test
    fun `deleteById should delete the article with the given ID`() {
        // Given
        val articleId = 1
        val jwt = "dummy-jwt"
        val userDetails = UserDetailsDTO(1, "FirstName", "LastName", "Username", "Role")
        val article = Article(1, "2023-06-25", Timestamp(System.currentTimeMillis()), "Article 1", 1)
        `when`(authorizationService.getUserDetails(jwt)).thenReturn(userDetails)
        `when`(articleRepository.findById(articleId)).thenReturn(Optional.of(article))

        // When
        articleService.deleteById(articleId, jwt)

        // Then
        verify(likeRepository).deleteAllByArticleId(articleId)
        verify(articleRepository).deleteById(articleId)
    }

    @Test
    fun `deleteById should throw ArticleNotFoundException when article is not found`() {
        // Given
        val articleId = 1
        val jwt = "dummy-jwt"
        val userDetails = UserDetailsDTO(1, "FirstName", "LastName", "Username", "Role")
        `when`(authorizationService.getUserDetails(jwt)).thenReturn(userDetails)
        `when`(articleRepository.findById(articleId)).thenReturn(Optional.ofNullable(null))

        // When & Then
        assertThrows(ArticleNotFoundException::class.java) {
            articleService.deleteById(articleId, jwt)
        }

        verify(articleRepository).findById(articleId)
        verify(articleRepository, never()).deleteById(any())
    }

    @Test
    fun `deleteById should throw UnauthorizedException when user is not authorized`() {
        // Given
        val articleId = 1
        val jwt = "dummy-jwt"
        val userDetails = UserDetailsDTO(1, "FirstName", "LastName", "Username", "Role")
        val article = Article(1, "2023-06-25", Timestamp(System.currentTimeMillis()), "Article 1", 2)
        `when`(authorizationService.getUserDetails(jwt)).thenReturn(userDetails)
        `when`(articleRepository.findById(articleId)).thenReturn(Optional.of(article))

        // When & Then
        assertThrows(UnauthorizedException::class.java) {
            articleService.deleteById(articleId, jwt)
        }

        verify(articleRepository, never()).deleteById(any())
    }

    @Test
    fun `findAllByAuthorId should return articles belonging to the author with the given ID`() {
        // Given
        val authorId = 1
        val articles = listOf(
            Article(1, "2023-06-25", Timestamp(System.currentTimeMillis()), "Article 1", authorId),
            Article(2, "2023-06-26", Timestamp(System.currentTimeMillis()), "Article 2", authorId)
        )
        `when`(articleRepository.findAllByAuthorIdOrderByDate(authorId)).thenReturn(articles)

        // When
        val result = articleService.findAllByAuthorId(authorId)

        // Then
        assertEquals(2, result.size)
        assertEquals("Article 1", result[0].text)
        assertEquals("Article 2", result[1].text)
    }

    @Test
    fun `deleteByAuthorId should delete all articles belonging to the author with the given ID`() {
        // Given
        val authorId = 1

        // When
        articleService.deleteByAuthorId(authorId)

        // Then
        verify(articleRepository).deleteAllByAuthorId(authorId)
    }
}
