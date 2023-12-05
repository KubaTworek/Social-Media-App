package pl.jakubtworek.notifications.service

import pl.jakubtworek.notifications.exception.NotificationBadRequestException
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.*
import org.mockito.Mockito.*
import pl.jakubtworek.notifications.client.service.ArticleApiService
import pl.jakubtworek.notifications.client.service.AuthorizationApiService
import pl.jakubtworek.notifications.controller.dto.NotificationResponse
import pl.jakubtworek.notifications.model.dto.ArticleDTO
import pl.jakubtworek.notifications.model.dto.UserDetailsDTO
import pl.jakubtworek.notifications.model.entity.Notification
import pl.jakubtworek.notifications.model.message.LikeMessage
import pl.jakubtworek.notifications.repository.NotificationRepository
import java.sql.Timestamp
import java.time.Instant
import java.util.Optional

class NotificationServiceTest {

    @Mock
    private lateinit var notificationRepository: NotificationRepository
    @Mock
    private lateinit var authorizationService: AuthorizationApiService
    @Mock
    private lateinit var articleService: ArticleApiService
    @Mock
    private lateinit var notificationResponseFactory: NotificationResponseFactory
    @Mock
    private lateinit var objectMapper: ObjectMapper
    private lateinit var notificationService: NotificationServiceImpl

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        notificationService = NotificationServiceImpl(
            notificationRepository,
            authorizationService,
            articleService,
            notificationResponseFactory,
            objectMapper
        )
    }

    @Test
    fun findAllNotificationsByUser_ReturnsListOfNotifications() {
        // Given
        val jwt = "jwt"
        val authorId = 1
        val article1 = ArticleDTO(1, "2023-06-27", Timestamp(System.currentTimeMillis()).toString(), "Article 1", authorId)
        val article2 = ArticleDTO(2, "2023-06-26", Timestamp(System.currentTimeMillis()).toString(), "Article 2", authorId)
        val notification1 = Notification(1, 1, authorId, Timestamp.from(Instant.now()), type = "LIKE")
        val notification2 = Notification(2, 2, authorId, Timestamp.from(Instant.now()), type = "LIKE")
        val userDetails = UserDetailsDTO(1, "FirstName", "LastName", "Username", "Role")
        val articles = listOf(article1, article2)
        val expectedResponse1 = NotificationResponse("FirstName LastName", "liked your article", "LIKE")
        val expectedResponse2 = NotificationResponse("FirstName LastName", "liked your article", "LIKE")

        `when`(authorizationService.getUserDetails(jwt)).thenReturn(userDetails)
        `when`(articleService.getArticlesByAuthor(authorId)).thenReturn(articles)
        `when`(notificationRepository.findAllByArticleIdOrderByTimestampDesc(1)).thenReturn(listOf(notification1))
        `when`(notificationRepository.findAllByArticleIdOrderByTimestampDesc(2)).thenReturn(listOf(notification2))
        `when`(notificationResponseFactory.createResponse(notification1)).thenReturn(expectedResponse1)
        `when`(notificationResponseFactory.createResponse(notification2)).thenReturn(expectedResponse2)

        // When
        val result = notificationService.findAllNotificationsByUser(jwt)

        // Then
        assertEquals(2, result.size)
        assertEquals(expectedResponse1, result[0])
        assertEquals(expectedResponse2, result[1])
        verify(notificationRepository, times(1)).findAllByArticleIdOrderByTimestampDesc(1)
        verify(notificationRepository, times(1)).findAllByArticleIdOrderByTimestampDesc(2)
        verify(notificationResponseFactory, times(1)).createResponse(notification1)
        verify(notificationResponseFactory, times(1)).createResponse(notification2)
    }

    @Test
    fun processLikeMessage_NotificationDoesNotExist_SaveNotification() {
        // Given
        val message = "{\"articleId\": 10, \"authorId\": 1, \"timestamp\": 12345}"
        val likeMessage = LikeMessage(Timestamp.from(Instant.now()), 1, 10)

        `when`(notificationRepository.findByArticleIdAndAuthorId(likeMessage.articleId, likeMessage.authorId))
            .thenReturn(Optional.ofNullable(null))
        `when`(objectMapper.readValue(message, LikeMessage::class.java)).thenReturn(likeMessage)

        // When
        notificationService.processLikeMessage(message)

        // Then
        verify(notificationRepository, times(1)).findByArticleIdAndAuthorId(likeMessage.articleId, likeMessage.authorId)
        verify(notificationRepository, times(1)).save(any(Notification::class.java))
    }

    @Test
    fun processLikeMessage_NotificationExists_ThrowsException() {
        // Given
        val message = "{\"articleId\": 10, \"authorId\": 1, \"timestamp\": 12345}"
        val likeMessage = LikeMessage(Timestamp.from(Instant.now()), 1, 10)
        val notification = Notification(0, 10, 1, Timestamp.from(Instant.now()), "LIKE")

        `when`(notificationRepository.findByArticleIdAndAuthorId(likeMessage.articleId, likeMessage.authorId))
            .thenReturn(Optional.of(notification))
        `when`(objectMapper.readValue(message, LikeMessage::class.java)).thenReturn(likeMessage)

        // When & Then
        assertThrows<pl.jakubtworek.notifications.exception.NotificationBadRequestException> {
            notificationService.processLikeMessage(message)
        }
    }
}