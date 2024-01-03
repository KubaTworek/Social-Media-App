package pl.jakubtworek.notifications.service

import org.springframework.stereotype.Component
import pl.jakubtworek.notifications.controller.dto.NotificationResponse
import pl.jakubtworek.notifications.external.ArticleApiService
import pl.jakubtworek.notifications.external.AuthorApiService
import pl.jakubtworek.notifications.model.entity.Activity

@Component
class NotificationResponseFactory(
    private val articleService: ArticleApiService,
    private val authorService: AuthorApiService
) {
    fun createResponse(activity: Activity): NotificationResponse {
        return when (activity.type) {
            "LIKE" -> createLikeResponse(activity)
            "ARTICLE" -> createArticleResponse(activity)
            "FOLLOW" -> createFollowResponse(activity)
            else -> throw IllegalStateException("Illegal state: ${activity.type}")
        }
    }

    private fun createLikeResponse(activity: Activity): NotificationResponse {
        val article = articleService.getArticleById(activity.targetId)
        val author = authorService.getAuthorById(activity.authorId)

        val authorName = "${author.firstName} ${author.lastName}"
        val message = "liked your article"
        return NotificationResponse(activity.id, authorName, message, article.text, activity.createAt)
    }

    private fun createArticleResponse(activity: Activity): NotificationResponse {
        val article = articleService.getArticleById(activity.targetId)
        val author = authorService.getAuthorById(activity.authorId)

        val authorName = "${author.firstName} ${author.lastName}"
        val message = "post an article"
        return NotificationResponse(activity.id, authorName, message, article.text, activity.createAt)
    }

    private fun createFollowResponse(activity: Activity): NotificationResponse {
        val follower = authorService.getAuthorById(activity.authorId)

        val authorName = "${follower.firstName} ${follower.lastName}"
        val message = "follows you"
        return NotificationResponse(activity.id, authorName, message, null, activity.createAt)
    }
}
