package com.example.notifications.service

import com.example.notifications.client.service.*
import com.example.notifications.controller.dto.NotificationResponse
import com.example.notifications.exception.NotificationBadRequestException
import com.example.notifications.model.dto.UserDetailsDTO
import com.example.notifications.model.entity.Notification
import com.example.notifications.model.message.LikeMessage
import com.example.notifications.repository.NotificationRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

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
        val articleIds = articleService.getArticlesByAuthor(userDetails.authorId)

        val notifications = articleIds.flatMap { article ->
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
