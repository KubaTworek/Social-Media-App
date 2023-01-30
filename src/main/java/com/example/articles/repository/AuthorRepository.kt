package com.example.articles.repository

import com.example.articles.entity.AuthorPost
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AuthorRepository : JpaRepository<AuthorPost, Int> {
    fun findByFirstNameAndLastName(firstName: String, lastName: String): Optional<AuthorPost>
}