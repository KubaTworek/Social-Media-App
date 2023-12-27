package pl.jakubtworek.notifications.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import pl.jakubtworek.common.Constants.ROLE_ADMIN
import pl.jakubtworek.common.Constants.ROLE_USER
import pl.jakubtworek.notifications.controller.dto.NotificationResponse
import pl.jakubtworek.notifications.exception.NotificationBadRequestException
import pl.jakubtworek.notifications.external.ArticleApiService
import pl.jakubtworek.notifications.external.AuthorizationApiService
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

    private val logger: Logger = LoggerFactory.getLogger(NotificationServiceImpl::class.java)

    override fun findAllNotifications(jwt: String): List<NotificationResponse> {
        logger.info("Finding all notifications")
        authorizationService.getUserDetailsAndValidate(jwt, ROLE_ADMIN)
        return notificationRepository.findAll()
            .map { mapToNotificationResponse(it) }
    }

    override fun findAllNotificationsByUser(jwt: String): List<NotificationResponse> {
        logger.info("Finding all notifications by user")
        val userDetails = authorizationService.getUserDetailsAndValidate(jwt, ROLE_USER)
        val articles = articleService.getArticlesByAuthor(userDetails.authorId)

        return articles.flatMap { article ->
            notificationRepository.findAllByArticleIdOrderByCreateAtDesc(article.id)
                .map { mapToNotificationResponse(it) }
        }
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
            val likeMessage = message.deserialize()
            if (notificationRepository.existsByArticleIdAndAuthorId(
                    likeMessage.articleId,
                    likeMessage.authorId
                )
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
            logger.info("Notification saved successfully")
        } catch (e: Exception) {
            logger.error("Error processing like message", e)
            throw e
        }
    }

    private fun mapToNotificationResponse(
        notification: Notification
    ): NotificationResponse {
        return notificationResponseFactory.createResponse(notification)
    }

    private fun String.deserialize(): LikeMessage =
        let { objectMapper.readValue(this, LikeMessage::class.java) }
}
