package pl.jakubtworek.notifications.repository

import org.springframework.data.jpa.repository.JpaRepository
import pl.jakubtworek.notifications.model.entity.Notification

interface NotificationRepository : JpaRepository<Notification, Int> {
    fun findAllByArticleIdOrderByCreateAtDesc(articleId: Int): List<Notification>
    fun existsByArticleIdAndAuthorId(articleId: Int, authorId: Int): Boolean
}
