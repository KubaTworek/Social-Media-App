package pl.jakubtworek.notifications.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import pl.jakubtworek.notifications.client.service.AuthorizationApiService
import pl.jakubtworek.notifications.controller.dto.NotificationResponse
import pl.jakubtworek.notifications.exception.NotificationBadRequestException
import pl.jakubtworek.notifications.model.entity.Notification
import pl.jakubtworek.notifications.model.message.LikeMessage
import pl.jakubtworek.notifications.repository.NotificationRepository

@Service
class NotificationServiceImpl(
    private val notificationRepository: NotificationRepository,
    private val authorizationService: AuthorizationApiService,
    private val articleService: pl.jakubtworek.notifications.client.service.ArticleApiService,
    private val notificationResponseFactory: NotificationResponseFactory,
    private val objectMapper: ObjectMapper
) : NotificationService {
    override fun findAllNotificationsByUser(jwt: String): List<NotificationResponse> {
        val userDetails = authorizationService.getUserDetails(jwt)
        val articles = articleService.getArticlesByAuthor(userDetails.authorId)

        val notifications = articles.flatMap { article ->
            notificationRepository.findAllByArticleIdOrderByTimestampDesc(article.id)
                .map { mapToNotificationResponse(it) }
        }

        return notifications
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
            timestamp = likeMessage.timestamp,
            type = "LIKE"
        )
        notificationRepository.save(notification)
    }

    private fun mapToNotificationResponse(
        notification: Notification
    ): NotificationResponse {
        return notificationResponseFactory.createResponse(notification)
    }

    private fun String.deserialize(): LikeMessage =
        let { objectMapper.readValue(this, LikeMessage::class.java) }
}