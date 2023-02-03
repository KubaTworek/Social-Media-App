package com.example.authors.repository

import com.example.authors.model.Author
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*


@Repository
interface AuthorRepository : JpaRepository<Author, Int> {
    fun findByFirstNameAndLastName(firstName: String, lastName: String): Optional<Author>
}