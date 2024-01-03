package pl.jakubtworek.notifications.service

import pl.jakubtworek.notifications.controller.dto.AuthorWithActivityResponse
import pl.jakubtworek.notifications.controller.dto.NotificationResponse

interface NotificationService {
    fun getAllNotificationsForAdmin(jwt: String): List<NotificationResponse>
    fun getAuthorActivities(authorId: Int): AuthorWithActivityResponse
    fun getAllNotificationsByUser(jwt: String): List<NotificationResponse>
    fun updateNotificationAuthor(jwt: String, notificationId: Int, authorId: Int)
}
