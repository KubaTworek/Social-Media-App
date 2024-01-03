package pl.jakubtworek.notifications.service

import org.springframework.stereotype.Component
import pl.jakubtworek.notifications.controller.dto.ActivityResponse
import pl.jakubtworek.notifications.external.ArticleApiService
import pl.jakubtworek.notifications.external.AuthorApiService
import pl.jakubtworek.notifications.model.entity.Activity

@Component
class ActivityResponseFactory(
    private val articleApiService: ArticleApiService,
    private val authorApiService: AuthorApiService
) {
    fun createResponse(activity: Activity): ActivityResponse {
        return when (activity.type) {
            "LIKE" -> createLikeResponse(activity)
            "ARTICLE" -> createArticleResponse(activity)
            "FOLLOW" -> createFollowResponse(activity)
            else -> throw IllegalStateException("Illegal state: ${activity.type}")
        }
    }

    private fun createLikeResponse(activity: Activity): ActivityResponse {
        val article = articleApiService.getArticleById(activity.targetId)
        val author = authorApiService.getAuthorById(activity.authorId)

        val authorName = "${author.firstName} ${author.lastName}"
        val message = "liked an article"
        return ActivityResponse(activity.id, authorName, null, message, article.text, activity.createAt)
    }

    private fun createArticleResponse(activity: Activity): ActivityResponse {
        val article = articleApiService.getArticleById(activity.targetId)
        val author = authorApiService.getAuthorById(activity.authorId)

        val authorName = "${author.firstName} ${author.lastName}"
        val message = "post an article"
        return ActivityResponse(activity.id, authorName, null, message, article.text, activity.createAt)
    }

    private fun createFollowResponse(activity: Activity): ActivityResponse {
        val follower = authorApiService.getAuthorById(activity.authorId)
        val followed = authorApiService.getAuthorById(activity.targetId)

        val authorName = "${follower.firstName} ${follower.lastName}"
        val message = "follows"
        val targetName = "${followed.firstName} ${followed.lastName}"
        return ActivityResponse(activity.id, authorName, targetName, message, null, activity.createAt)
    }
}
