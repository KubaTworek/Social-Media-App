package com.example.articles.repository

import com.example.articles.entity.Author
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface AuthorRepository : JpaRepository<Author, Int> {
    fun findByFirstNameAndLastName(firstName: String, lastName: String): Optional<Author>
}