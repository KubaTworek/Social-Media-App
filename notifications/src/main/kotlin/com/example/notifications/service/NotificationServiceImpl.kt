package com.example.notifications.service

import com.example.notifications.client.service.AuthorizationApiService
import com.example.notifications.controller.dto.NotificationResponse
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
    private val notificationResponseFactory: NotificationResponseFactory,
    private val objectMapper: ObjectMapper
) : NotificationService {
    override fun findAllNotificationsByUser(jwt: String): List<NotificationResponse> {
        val userDetails = authorizationService.getUserDetails(jwt)
        return notificationRepository.findAllByAuthorId(userDetails.authorId)
            .map { mapToNotificationResponse(it, userDetails) }
            .toList()
    }

    @KafkaListener(topics = ["t-like"])
    override fun processLikeMessage(message: String) {
        val likeMessage = message.deserialize()
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
        notification: Notification,
        userDetails: UserDetailsDTO
    ): NotificationResponse {
        return notificationResponseFactory.createResponse(notification, userDetails)
    }

    private fun String.deserialize(): LikeMessage =
        let { objectMapper.readValue(this, LikeMessage::class.java) }
}
