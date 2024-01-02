package pl.jakubtworek.notifications.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.coyote.http11.Constants.a
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import pl.jakubtworek.notifications.controller.dto.ActivityResponse
import pl.jakubtworek.common.Constants.ROLE_ADMIN
import pl.jakubtworek.common.Constants.ROLE_USER
import pl.jakubtworek.notifications.controller.dto.NotificationResponse
import pl.jakubtworek.notifications.exception.NotificationBadRequestException
import pl.jakubtworek.notifications.external.ArticleApiService
import pl.jakubtworek.notifications.external.AuthorApiService
import pl.jakubtworek.notifications.external.AuthorizationApiService
import pl.jakubtworek.notifications.model.entity.Activity
import pl.jakubtworek.notifications.model.message.ArticleMessage
import pl.jakubtworek.notifications.model.message.FollowMessage
import pl.jakubtworek.notifications.model.message.LikeMessage
import pl.jakubtworek.notifications.repository.NotificationRepository

@Service
class NotificationServiceImpl(
    private val notificationRepository: NotificationRepository,
    private val authorizationService: AuthorizationApiService,
    private val articleService: ArticleApiService,
    private val authorService: AuthorApiService,
    private val notificationResponseFactory: NotificationResponseFactory,
    private val activityResponseFactory: ActivityResponseFactory,
    private val objectMapper: ObjectMapper
) : NotificationService {

    private val logger: Logger = LoggerFactory.getLogger(NotificationServiceImpl::class.java)

    override fun findAllNotifications(jwt: String): List<NotificationResponse> {
        logger.info("Finding all notifications")
        authorizationService.getUserDetailsAndValidate(jwt, ROLE_ADMIN)
        return notificationRepository.findAllByTypeOrderByCreateAtDesc("LIKE")
            .map { mapToNotificationResponse(it) }
    }

    override fun findAllAuthorActivities(authorId: Int): List<ActivityResponse> {
        logger.info("Finding activities for author with ID: $authorId")
        return notificationRepository.findAllByAuthorIdOrderByCreateAtDesc(authorId)
            .map { mapToActivityResponse(it) }
    }

    override fun findAllNotificationsByUser(jwt: String): List<NotificationResponse> {
        logger.info("Finding all notifications by user")
        val userDetails = authorizationService.getUserDetailsAndValidate(jwt, ROLE_USER)
        val articles = articleService.getArticlesByAuthor(userDetails.authorId)
        val author = authorService.getAuthorById(userDetails.authorId)

        val articleNotifications = author.following.flatMap { authorId ->
            notificationRepository.findAllByAuthorIdAndTypeOrderByCreateAtDesc(authorId, "ARTICLE")
                .map { mapToNotificationResponse(it) }
        }
        val followNotifications = notificationRepository.findAllByTargetIdAndTypeOrderByCreateAtDesc(userDetails.authorId, "FOLLOW")
            .map { mapToNotificationResponse(it) }
        val likeNotifications = articles.flatMap { article ->
            notificationRepository.findAllByTargetIdAndTypeOrderByCreateAtDesc(article.id, "LIKE")
                .map { mapToNotificationResponse(it) }
        }
        return articleNotifications + followNotifications + likeNotifications
            .sortedByDescending { it.createAt }
    }

    override fun update(jwt: String, notificationId: Int, authorId: Int) {
        logger.info("Updating notification with ID: $notificationId")
        authorizationService.getUserDetailsAndValidate(jwt, ROLE_ADMIN)
        notificationRepository.findById(notificationId)
            .ifPresent { notification ->
                notification.authorId = authorId
                notificationRepository.save(notification)
                logger.info("Notification updated successfully")
            }
    }

    @KafkaListener(topics = ["t-like"])
    override fun processLikeMessage(message: String) {
        logger.info("Processing like message: $message")
        try {
            val likeMessage = message.deserializeToLike()
            if (notificationRepository.existsByTargetIdAndAuthorIdAndType(
                    likeMessage.articleId,
                    likeMessage.authorId,
                    "LIKE"
                )
            ) {
                throw NotificationBadRequestException("Notification was already received")
            }
            val activity = Activity(
                id = 0,
                targetId = likeMessage.articleId,
                authorId = likeMessage.authorId,
                createAt = likeMessage.timestamp,
                type = "LIKE"
            )
            notificationRepository.save(activity)
            logger.info("Notification saved successfully")
        } catch (e: Exception) {
            logger.error("Error processing like message", e)
            throw e
        }
    }

    @KafkaListener(topics = ["t-article"])
    override fun processArticleMessage(message: String) {
        logger.info("Processing article message: $message")
        try {
            val articleMessage = message.deserializeToArticle()
            if (notificationRepository.existsByTargetIdAndType(
                    articleMessage.articleId,
                    "ARTICLE"
                )
            ) {
                throw NotificationBadRequestException("Notification was already received")
            }
            val activity = Activity(
                id = 0,
                targetId = articleMessage.articleId,
                authorId = articleMessage.authorId,
                createAt = articleMessage.timestamp,
                type = "ARTICLE"
            )
            notificationRepository.save(activity)
            logger.info("Notification saved successfully")
        } catch (e: Exception) {
            logger.error("Error processing article message", e)
            throw e
        }
    }

    @KafkaListener(topics = ["t-follow"])
    override fun processFollowMessage(message: String) {
        logger.info("Processing follow message: $message")
        try {
            val followMessage = message.deserializeToFollow()
            if (notificationRepository.existsByTargetIdAndAuthorIdAndType(
                    followMessage.followedId,
                    followMessage.followerId,
                "FOLLOW"
                )
            ) {
                throw NotificationBadRequestException("Notification was already received")
            }
            val activity = Activity(
                id = 0,
                targetId = followMessage.followedId,
                authorId = followMessage.followerId,
                createAt = followMessage.timestamp,
                type = "FOLLOW"
            )
            notificationRepository.save(activity)
            logger.info("Notification saved successfully")
        } catch (e: Exception) {
            logger.error("Error processing follow message", e)
            throw e
        }
    }

    private fun mapToNotificationResponse(
        activity: Activity
    ): NotificationResponse {
        return notificationResponseFactory.createResponse(activity)
    }

    private fun mapToActivityResponse(
        activity: Activity
    ): ActivityResponse {
        return activityResponseFactory.createResponse(activity)
    }

    private fun String.deserializeToLike(): LikeMessage =
        let { objectMapper.readValue(this, LikeMessage::class.java) }

    private fun String.deserializeToArticle(): ArticleMessage =
        let { objectMapper.readValue(this, ArticleMessage::class.java) }

    private fun String.deserializeToFollow(): FollowMessage =
        let { objectMapper.readValue(this, FollowMessage::class.java) }
}
