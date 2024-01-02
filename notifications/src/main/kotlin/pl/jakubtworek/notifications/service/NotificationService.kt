package pl.jakubtworek.notifications.service

import pl.jakubtworek.notifications.controller.dto.ActivityResponse
import pl.jakubtworek.notifications.controller.dto.NotificationResponse

interface NotificationService {
    fun findAllNotifications(jwt: String): List<NotificationResponse>
    fun findAllAuthorActivities(authorId: Int): List<ActivityResponse>
    fun findAllNotificationsByUser(jwt: String): List<NotificationResponse>
    fun update(jwt: String, notificationId: Int, authorId: Int)
    fun processLikeMessage(message: String)
    fun processArticleMessage(message: String)
    fun processFollowMessage(message: String)
}
