package pl.jakubtworek.notifications.kafka

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import pl.jakubtworek.common.Constants.ARTICLE_TYPE
import pl.jakubtworek.common.Constants.FOLLOW_TYPE
import pl.jakubtworek.common.Constants.LIKE_TYPE
import pl.jakubtworek.notifications.exception.NotificationBadRequestException
import pl.jakubtworek.notifications.model.entity.Activity
import pl.jakubtworek.notifications.model.message.ArticleMessage
import pl.jakubtworek.notifications.model.message.FollowMessage
import pl.jakubtworek.notifications.model.message.LikeMessage
import pl.jakubtworek.notifications.repository.NotificationRepository
import java.sql.Timestamp

@Service
class MessageHandler(
    private val notificationRepository: NotificationRepository,
) {
    private val logger: Logger = LoggerFactory.getLogger(MessageHandler::class.java)

    fun processArticleMessage(message: ArticleMessage) {
        if (notificationRepository.existsByTargetIdAndType(
                message.articleId,
                ARTICLE_TYPE
            )
        ) {
            throw NotificationBadRequestException("Notification was already received")
        }
        saveActivity(message.articleId, message.authorId, ARTICLE_TYPE, message.timestamp)
        logger.info("Notification saved successfully")
    }

    fun processLikeMessage(message: LikeMessage) {
        if (notificationRepository.existsByTargetIdAndAuthorIdAndType(
                message.articleId,
                message.authorId,
                LIKE_TYPE
            )
        ) {
            throw NotificationBadRequestException("Notification was already received")
        }
        saveActivity(message.articleId, message.authorId, LIKE_TYPE, message.timestamp)
        logger.info("Notification saved successfully")
    }

    fun processFollowMessage(message: FollowMessage) {
        if (notificationRepository.existsByTargetIdAndAuthorIdAndType(
                message.followedId,
                message.followerId,
                FOLLOW_TYPE
            )
        ) {
            throw NotificationBadRequestException("Notification was already received")
        }
        saveActivity(message.followedId, message.followerId, FOLLOW_TYPE, message.timestamp)
        logger.info("Notification saved successfully")
    }

    private fun saveActivity(targetId: Int, authorId: Int, type: String, timestamp: Timestamp) {
        val activity = Activity(
            id = 0,
            targetId = targetId,
            authorId = authorId,
            createAt = timestamp,
            type = type
        )
        notificationRepository.save(activity)
    }
}