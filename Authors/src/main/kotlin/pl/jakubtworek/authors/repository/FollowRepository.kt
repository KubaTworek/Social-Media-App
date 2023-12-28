package pl.jakubtworek.authors.repository

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.jakubtworek.authors.entity.Follow


@Repository
interface FollowRepository : JpaRepository<Follow, Int> {
    @Transactional
    fun deleteByFollowerIdAndFollowingId(authorId: Int, followingId: Int)
}
