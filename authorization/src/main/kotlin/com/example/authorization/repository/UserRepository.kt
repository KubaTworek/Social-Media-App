package com.example.authorization.repository

import com.example.authorization.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Int> {
    fun findUserByUsername(username: String): Optional<User>
    fun existsByUsername(username: String): Boolean
}
