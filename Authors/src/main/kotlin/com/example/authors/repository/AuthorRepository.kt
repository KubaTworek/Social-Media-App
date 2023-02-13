package com.example.authors.repository

import com.example.authors.model.entity.Author
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface AuthorRepository : JpaRepository<Author, Int> {
    fun findAllByFirstNameContainingOrLastNameContaining(keyword1: String, keyword2: String): List<Author>
}