package pl.jakubtworek.notifications.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import pl.jakubtworek.notifications.external.AuthorizationApiService
import pl.jakubtworek.notifications.controller.dto.NotificationResponse
import pl.jakubtworek.notifications.exception.NotificationBadRequestException
import pl.jakubtworek.notifications.external.ArticleApiService
import pl.jakubtworek.notifications.model.entity.Notification
import pl.jakubtworek.notifications.model.message.LikeMessage
import pl.jakubtworek.notifications.repository.NotificationRepository

@Service
class NotificationServiceImpl(
    private val notificationRepository: NotificationRepository,
    private val authorizationService: AuthorizationApiService,
    private val articleService: ArticleApiService,
    private val notificationResponseFactory: NotificationResponseFactory,
    private val objectMapper: ObjectMapper
) : NotificationService {
    override fun findAllNotificationsByUser(jwt: String): List<NotificationResponse> {
        val userDetails = authorizationService.getUserDetails(jwt)
        val articles = articleService.getArticlesByAuthor(userDetails.authorId)

        val notifications = articles.flatMap { article ->
            notificationRepository.findAllByArticleIdOrderByCreateAtDesc(article.id)
                .map { mapToNotificationResponse(it) }
        }

        return notifications
    }

    override fun update(jwt: String, notificationId: Int, authorId: Int) {
        val userDetails = authorizationService.getUserDetails(jwt)
        if (userDetails.role == "ROLE_ADMIN") {
            notificationRepository.findById(notificationId)
                .ifPresent { notification ->
                    notification.authorId = authorId
                    notificationRepository.save(notification)
                }
        }
    }

    @KafkaListener(topics = ["t-like"])
    override fun processLikeMessage(message: String) {
        val likeMessage = message.deserialize()
        if (notificationRepository.findByArticleIdAndAuthorId(
                likeMessage.articleId,
                likeMessage.authorId
            ).isPresent
        ) {
            throw NotificationBadRequestException("Notification was already sent")
        }
        val notification = Notification(
            id = 0,
            articleId = likeMessage.articleId,
            authorId = likeMessage.authorId,
            createAt = likeMessage.timestamp,
            type = "LIKE"
        )
        notificationRepository.save(notification)
    }

    override fun findAllNotifications(jwt: String): List<NotificationResponse> {
        return notificationRepository.findAll().map { mapToNotificationResponse(it) }
    }

    private fun mapToNotificationResponse(
        notification: Notification
    ): NotificationResponse {
        return notificationResponseFactory.createResponse(notification)
    }

    private fun String.deserialize(): LikeMessage =
        let { objectMapper.readValue(this, LikeMessage::class.java) }
}
