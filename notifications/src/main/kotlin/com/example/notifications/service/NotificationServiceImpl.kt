package com.example.notifications.service

import com.example.notifications.model.entity.Notification
import com.example.notifications.repository.NotificationRepository
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import java.sql.Timestamp

@Service
class NotificationServiceImpl(
    private val notificationRepository: NotificationRepository
) : NotificationService {
    override fun findAllNotificationsByUser(jwt: String): List<Notification> =
        notificationRepository.findAll()

    @KafkaListener(topics= ["like"])
    override fun getLike(message: String) {
        val notification = Notification(
            0,
            message,
            Timestamp(System.currentTimeMillis())
        )
        notificationRepository.save(notification)
    }
}