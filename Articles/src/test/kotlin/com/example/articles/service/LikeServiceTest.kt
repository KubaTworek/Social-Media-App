package com.example.articles.service

import com.example.articles.client.service.AuthorApiService
import com.example.articles.client.service.AuthorizationApiService
import com.example.articles.kafka.service.KafkaLikeService
import com.example.articles.model.dto.AuthorDTO
import com.example.articles.model.dto.UserDetailsDTO
import com.example.articles.model.entity.Like
import com.example.articles.repository.LikeRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.sql.Timestamp
import java.time.Instant
import kotlin.test.assertEquals


class LikeServiceTest {

    @Mock
    private lateinit var likeRepository: LikeRepository

    @Mock
    private lateinit var authorizationService: AuthorizationApiService

    @Mock
    private lateinit var authorApiService: AuthorApiService

    @Mock
    private lateinit var kafkaLikeService: KafkaLikeService

    @Captor
    private lateinit var likeCaptor: ArgumentCaptor<Like>
    private lateinit var likeService: LikeService

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        likeService = LikeServiceImpl(
            likeRepository,
            authorizationService,
            authorApiService,
            kafkaLikeService
        )
    }

    @Test
    fun `like should save like and send like message`() {
        // Given
        val articleId = 1
        val jwt = "dummy-jwt"
        val userDetails = UserDetailsDTO(1, "FirstName", "LastName", "Username", "Role")
        val timestamp = Timestamp(System.currentTimeMillis())
        val expectedLike = Like(id = 0, timestamp = timestamp, authorId = 1, articleId = articleId)

        `when`(authorizationService.getUserDetails(jwt)).thenReturn(userDetails)

        // When
        likeService.like(articleId, jwt)

        // Then
        verify(likeRepository, times(1)).save(likeCaptor.capture())
        assertEquals(expectedLike.authorId, likeCaptor.value.authorId)
        assertEquals(expectedLike.articleId, likeCaptor.value.articleId)
    }

    @Test
    fun `getLikeInfo should return the like info response with author names`() {
        // Given
        val articleId = 1
        val likes = listOf(
            Like(1, Timestamp.from(Instant.now()), 1, 1),
            Like(2, Timestamp.from(Instant.now()), 2, 1)
        )
        val expectedAuthorNames = listOf("John Doe", "Jane Smith")

        `when`(likeRepository.findByArticleId(articleId)).thenReturn(likes)
        `when`(authorApiService.getAuthorById(1)).thenReturn(AuthorDTO(1, "John", "Doe", "johndoe"))
        `when`(authorApiService.getAuthorById(2)).thenReturn(AuthorDTO(2, "Jane", "Smith", "janesmith"))

        // When
        val likeInfoResponse = likeService.getLikeInfo(articleId)

        // Then
        assertEquals(expectedAuthorNames, likeInfoResponse.users)

        verify(likeRepository, times(1)).findByArticleId(articleId)
        verify(authorApiService, times(1)).getAuthorById(1)
        verify(authorApiService, times(1)).getAuthorById(2)
    }
}