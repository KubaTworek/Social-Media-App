package pl.jakubtworek.authors.service

import pl.jakubtworek.authors.external.ArticleApiService
import pl.jakubtworek.authors.controller.dto.AuthorRequest
import pl.jakubtworek.authors.exception.AuthorNotFoundException
import pl.jakubtworek.authors.entity.Author
import pl.jakubtworek.authors.repository.AuthorRepository
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.mockito.*
import org.mockito.Mockito.*
import java.util.*

class AuthorServiceTest {
    @Mock
    private lateinit var authorRepository: AuthorRepository

    @Mock
    private lateinit var articleApiService: ArticleApiService

    private lateinit var authorService: AuthorService

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        authorService = AuthorServiceImpl(authorRepository, articleApiService)
    }

    @Test
    fun `findById should return the author with the given ID`() {
        // Given
        val authorId = 1
        val author = Author(authorId, "John", "Doe", "johndoe")
        `when`(authorRepository.findById(authorId)).thenReturn(Optional.of(author))

        // When
        val result = authorService.findById(authorId)

        // Then
        assertEquals(authorId, result.id)
        assertEquals("John", result.firstName)
        assertEquals("Doe", result.lastName)
        assertEquals("johndoe", result.username)
    }

    @Test
    fun `findById should throw AuthorNotFoundException when author is not found`() {
        // Given
        val authorId = 1
        `when`(authorRepository.findById(authorId)).thenReturn(Optional.ofNullable(null))

        // When & Then
        assertThrows(AuthorNotFoundException::class.java) {
            authorService.findById(authorId)
        }
    }

    @Test
    fun `findByUsername should return the author with the given username`() {
        // Given
        val username = "johndoe"
        val author = Author(1, "John", "Doe", username)
        `when`(authorRepository.findAuthorByUsername(username)).thenReturn(author)

        // When
        val result = authorService.findByUsername(username)

        // Then
        assertEquals(1, result.id)
        assertEquals("John", result.firstName)
        assertEquals("Doe", result.lastName)
        assertEquals(username, result.username)
    }

    @Test
    fun `findByUsername should throw AuthorNotFoundException when author is not found`() {
        // Given
        val username = "johndoe"
        `when`(authorRepository.findAuthorByUsername(username)).thenReturn(null)

        // When & Then
        assertThrows(AuthorNotFoundException::class.java) {
            authorService.findByUsername(username)
        }
    }

    @Test
    fun `save should create and save a new author`() {
        // Given
        val authorRequest = AuthorRequest("John", "Doe", "johndoe")
        val expectedAuthor = Author(1, "John", "Doe", "johndoe")
        `when`(authorRepository.save(any(Author::class.java))).thenReturn(expectedAuthor)

        // When
        authorService.save(authorRequest)

        // Then
        verify(authorRepository).save(any(Author::class.java))
    }

    @Test
    fun `deleteById should delete the author with the given ID and delete associated articles`() {
        // Given
        val authorId = 1

        // When
        authorService.deleteById(authorId)

        // Then
        verify(authorRepository).deleteById(authorId)
        verify(articleApiService).deleteArticlesByAuthorId(authorId)
    }
}
