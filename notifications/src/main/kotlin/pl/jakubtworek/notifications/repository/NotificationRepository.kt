package pl.jakubtworek.notifications.repository

import org.springframework.data.jpa.repository.JpaRepository
import pl.jakubtworek.notifications.model.entity.Activity

interface NotificationRepository : JpaRepository<Activity, Int> {
    fun findAllByTypeOrderByCreateAtDesc(type: String): List<Activity>
    fun findAllByAuthorIdOrderByCreateAtDesc(authorId: Int): List<Activity>
    fun findAllByTargetIdAndTypeOrderByCreateAtDesc(targetId: Int, type: String): List<Activity>
    fun existsByTargetIdAndAuthorIdAndType(targetId: Int, authorId: Int, type: String): Boolean
    fun existsByTargetIdAndType(targetId: Int, type: String): Boolean
    fun findAllByAuthorIdAndTypeOrderByCreateAtDesc(authorId: Int, s: String): List<Activity>
}
