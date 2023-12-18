package pl.jakubtworek.articles.service

import pl.jakubtworek.articles.client.service.AuthorApiService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import pl.jakubtworek.articles.client.service.AuthorizationApiService
import pl.jakubtworek.articles.controller.dto.ArticleRequest
import pl.jakubtworek.articles.model.dto.AuthorDTO
import pl.jakubtworek.articles.model.dto.UserDetailsDTO
import pl.jakubtworek.articles.model.entity.Article
import pl.jakubtworek.articles.repository.ArticleRepository
import pl.jakubtworek.articles.repository.LikeRepository
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
            Article(1, Timestamp(System.currentTimeMillis()), "Article 1", 1),
            Article(2, Timestamp(System.currentTimeMillis()), "Article 2", 2),
            Article(3, Timestamp(System.currentTimeMillis()), "Article 3", 1)
        )
        val pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "timestamp"))
        val articlesPage: Page<Article> = PageImpl(articles, pageRequest, articles.size.toLong())

        `when`(articleRepository.findAll(any<PageRequest>())).thenReturn(articlesPage)
        `when`(authorService.getAuthorById(any(Int::class.java))).thenReturn(author)
        `when`(likeRepository.countLikesByArticleId(any(Int::class.java))).thenReturn(5)

        // When
        val result = articleService.findAllOrderByCreatedTimeDesc(0, 5)

        // Then
        assertEquals(3, result.size)
        assertEquals("Article 1", result[0].text)
        assertEquals("Article 2", result[1].text)
        assertEquals("Article 3", result[2].text)
    }

    @Test
    fun `findById should return the article with the given ID`() {
        // Given
        val articleId = 1
        val article = Article(1, Timestamp(System.currentTimeMillis()), "Article 1", 1)
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
        assertThrows(pl.jakubtworek.articles.exception.ArticleNotFoundException::class.java) {
            articleService.findById(articleId)
        }
    }

    @Test
    fun `save should create and save a new article`() {
        // Given
        val articleRequest = ArticleRequest("Text")
        val jwt = "dummy-jwt"
        val userDetails = UserDetailsDTO(1, "FirstName", "LastName", "Username", "Role")
        val expectedArticle = Article(1, Timestamp(System.currentTimeMillis()), "Article 1", 1)
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
        val article = Article(1, Timestamp(System.currentTimeMillis()), "Article 1", 1)
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
        assertThrows(pl.jakubtworek.articles.exception.ArticleNotFoundException::class.java) {
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
        val article = Article(1, Timestamp(System.currentTimeMillis()), "Article 1", 2)
        `when`(authorizationService.getUserDetails(jwt)).thenReturn(userDetails)
        `when`(articleRepository.findById(articleId)).thenReturn(Optional.of(article))

        // When & Then
        assertThrows(pl.jakubtworek.articles.exception.UnauthorizedException::class.java) {
            articleService.deleteById(articleId, jwt)
        }

        verify(articleRepository, never()).deleteById(any())
    }

    @Test
    fun `findAllByAuthorId should return articles belonging to the author with the given ID`() {
        // Given
        val authorId = 1
        val articles = listOf(
            Article(1, Timestamp(System.currentTimeMillis()), "Article 1", authorId),
            Article(2, Timestamp(System.currentTimeMillis()), "Article 2", authorId)
        )
        `when`(articleRepository.findAllByAuthorIdOrderByCreateAt(authorId)).thenReturn(articles)

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
