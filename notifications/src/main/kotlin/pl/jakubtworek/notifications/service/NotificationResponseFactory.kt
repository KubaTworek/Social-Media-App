package pl.jakubtworek.notifications.service

import org.springframework.stereotype.Component
import pl.jakubtworek.notifications.client.service.AuthorApiService
import pl.jakubtworek.notifications.controller.dto.NotificationResponse
import pl.jakubtworek.notifications.model.entity.Notification

@Component
class NotificationResponseFactory(
    private val articleApiService: pl.jakubtworek.notifications.client.service.ArticleApiService,
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