package com.example.notifications.service

import com.example.notifications.client.service.ArticleApiService
import com.example.notifications.controller.dto.NotificationResponse
import com.example.notifications.model.dto.*
import com.example.notifications.model.entity.Notification
import org.springframework.stereotype.Component

@Component
class NotificationResponseFactory(
    private val articleApiService: ArticleApiService
) {
    fun createResponse(notification: Notification, userDetails: UserDetailsDTO): NotificationResponse {
        return when (notification.type) {
            "LIKE" -> createLikeResponse(notification, userDetails)
            else -> createErrorResponse()
        }
    }

    private fun createLikeResponse(notification: Notification, userDetails: UserDetailsDTO): NotificationResponse {
        val article = articleApiService.getArticleById(notification.articleId)
        val authorName = "${userDetails.firstName} ${userDetails.lastName}"
        val message = "liked your article"
        return NotificationResponse(authorName, message, article.text)
    }

    private fun createErrorResponse(): NotificationResponse {
        return NotificationResponse("Error", "", "")
    }
}