package com.example.notifications.repository

import com.example.notifications.model.entity.Notification
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface NotificationRepository : JpaRepository<Notification, Int> {
    fun findAllByArticleIdOrderByTimestampDesc(articleId: Int): List<Notification>
    fun findByArticleIdAndAuthorId(articleId: Int, authorId: Int): Optional<Notification>
}