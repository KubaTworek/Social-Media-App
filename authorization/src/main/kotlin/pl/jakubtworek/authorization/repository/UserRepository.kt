package pl.jakubtworek.authorization.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.jakubtworek.authorization.entity.User

@Repository
interface UserRepository : JpaRepository<User, String> {
    fun findUserByUsername(username: String): User?
    fun existsByUsername(username: String): Boolean
    fun deleteByUsername(username: String)
}
