package com.example.notifications.repository

import com.example.notifications.model.entity.Notification
import org.springframework.data.jpa.repository.JpaRepository

interface NotificationRepository : JpaRepository<Notification, Int> {
    fun findAllByAuthorId(authorId: Int): List<Notification>
}