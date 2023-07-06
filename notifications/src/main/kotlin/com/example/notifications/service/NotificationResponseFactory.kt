package com.example.notifications.service

import com.example.notifications.client.service.*
import com.example.notifications.controller.dto.NotificationResponse
import com.example.notifications.model.dto.*
import com.example.notifications.model.entity.Notification
import org.springframework.stereotype.Component

@Component
class NotificationResponseFactory(
    private val articleApiService: ArticleApiService,
    private val authorApiService: AuthorApiService
) {
    fun createResponse(notification: Notification): NotificationResponse {
        return when (notification.type) {
            "LIKE" -> createLikeResponse(notification)
            else -> createErrorResponse()
        }
    }

    private fun createLikeResponse(notification: Notification): NotificationResponse {
        val article = articleApiService.getArticleById(notification.articleId)
        val author = authorApiService.getAuthorById(notification.authorId)

        val authorName = "${author.firstName} ${author.lastName}"
        val message = "liked your article"
        return NotificationResponse(authorName, message, article.text)
    }

    private fun createErrorResponse(): NotificationResponse {
        return NotificationResponse("Error", "", "")
    }
}