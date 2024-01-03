package pl.jakubtworek.notifications.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import pl.jakubtworek.common.Constants.ARTICLE_TYPE
import pl.jakubtworek.common.Constants.FOLLOW_TYPE
import pl.jakubtworek.common.Constants.LIKE_TYPE
import pl.jakubtworek.common.Constants.ROLE_ADMIN
import pl.jakubtworek.common.Constants.ROLE_USER
import pl.jakubtworek.common.model.AuthorDTO
import pl.jakubtworek.common.model.UserDetailsDTO
import pl.jakubtworek.notifications.controller.dto.ActivityResponse
import pl.jakubtworek.notifications.controller.dto.AuthorWithActivityResponse
import pl.jakubtworek.notifications.controller.dto.NotificationResponse
import pl.jakubtworek.notifications.external.ArticleApiService
import pl.jakubtworek.notifications.external.AuthorApiService
import pl.jakubtworek.notifications.external.AuthorizationApiService
import pl.jakubtworek.notifications.repository.NotificationRepository

@Service
class NotificationServiceImpl(
    private val notificationRepository: NotificationRepository,
    private val authorizationService: AuthorizationApiService,
    private val articleService: ArticleApiService,
    private val authorService: AuthorApiService,
    private val notificationResponseFactory: NotificationResponseFactory,
    private val activityResponseFactory: ActivityResponseFactory
) : NotificationService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun getAllNotificationsForAdmin(jwt: String): List<NotificationResponse> { // fixme: delete po obronie
        logger.info("Fetching all notifications for admin")
        authorizationService.getUserDetailsAndValidate(jwt, ROLE_ADMIN)
        return fetchLikeNotifications()
    }

    override fun getAuthorActivities(authorId: Int): AuthorWithActivityResponse {
        logger.info("Fetching activities for author with ID: $authorId")
        val author = authorService.getAuthorById(authorId)
        val activities = fetchAuthorActivities(authorId)
        return createAuthorWithActivityResponse(author, activities)
    }

    override fun getAllNotificationsByUser(jwt: String): List<NotificationResponse> {
        logger.info("Fetching all notifications by user")
        val userDetails = authorizationService.getUserDetailsAndValidate(jwt, ROLE_USER)

        val articleNotifications = findArticleNotifications(userDetails.authorId)
        val followNotifications = findFollowNotifications(userDetails.authorId)
        val likeNotifications = findLikeNotifications(userDetails)

        return combineAndSortNotifications(articleNotifications, followNotifications, likeNotifications)
    }

    override fun updateNotificationAuthor(jwt: String, notificationId: Int, authorId: Int) {
        logger.info("Updating notification author with ID: $notificationId")
        authorizationService.getUserDetailsAndValidate(jwt, ROLE_ADMIN)
        notificationRepository.findById(notificationId)
            .ifPresent { notification ->
                notification.authorId = authorId
                notificationRepository.save(notification)
                logger.info("Notification author updated successfully")
            }
    }

    private fun createAuthorWithActivityResponse(
        author: AuthorDTO,
        activities: List<ActivityResponse>
    ): AuthorWithActivityResponse =
        AuthorWithActivityResponse(
            id = author.id,
            firstName = author.firstName,
            lastName = author.lastName,
            username = author.username,
            following = author.following,
            followers = author.followers,
            activities = activities
        )

    private fun fetchLikeNotifications(): List<NotificationResponse> =
        notificationRepository.findAllByTypeOrderByCreateAtDesc(LIKE_TYPE)
            .map(notificationResponseFactory::createResponse)

    private fun fetchAuthorActivities(authorId: Int): List<ActivityResponse> =
        notificationRepository.findAllByAuthorIdOrderByCreateAtDesc(authorId)
            .map(activityResponseFactory::createResponse)

    private fun findArticleNotifications(authorId: Int): List<NotificationResponse> =
        authorService.getAuthorById(authorId).following
            .flatMap { notificationRepository.findAllByAuthorIdAndTypeOrderByCreateAtDesc(it, ARTICLE_TYPE) }
            .map(notificationResponseFactory::createResponse)

    private fun findFollowNotifications(authorId: Int): List<NotificationResponse> =
        notificationRepository.findAllByTargetIdAndTypeOrderByCreateAtDesc(authorId, FOLLOW_TYPE)
            .map(notificationResponseFactory::createResponse)

    private fun findLikeNotifications(userDetails: UserDetailsDTO): List<NotificationResponse> {
        val articles = articleService.getArticlesByAuthor(userDetails.authorId)
        return articles.flatMap { article ->
            notificationRepository.findAllByTargetIdAndTypeOrderByCreateAtDesc(article.id, LIKE_TYPE)
                .map(notificationResponseFactory::createResponse)
        }
    }

    private fun combineAndSortNotifications(
        articleNotifications: List<NotificationResponse>,
        followNotifications: List<NotificationResponse>,
        likeNotifications: List<NotificationResponse>
    ): List<NotificationResponse> =
        (articleNotifications + followNotifications + likeNotifications).sortedByDescending { it.createAt }
}
