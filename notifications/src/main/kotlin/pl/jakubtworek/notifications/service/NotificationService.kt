package pl.jakubtworek.notifications.service

import pl.jakubtworek.notifications.controller.dto.NotificationResponse

interface NotificationService {
    fun findAllNotificationsByUser(jwt: String): List<NotificationResponse>
    fun processLikeMessage(message: String)
}