package pl.jakubtworek.notifications.service

import pl.jakubtworek.notifications.controller.dto.NotificationResponse

interface NotificationService {
    fun findAllNotifications(jwt: String): List<NotificationResponse>
    fun findAllNotificationsByUser(jwt: String): List<NotificationResponse>
    fun update(jwt: String, notificationId: Int, authorId: Int)
    fun processLikeMessage(message: String)
}
