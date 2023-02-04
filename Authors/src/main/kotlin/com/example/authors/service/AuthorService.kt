package com.example.authors.service

import com.example.authors.controller.AuthorRequest
import com.example.authors.model.Author
import java.util.*

interface AuthorService {
    fun findAllAuthors(): List<Author>
    fun findById(theId: Int): Optional<Author>
    fun findByName(firstName: String, lastName: String): Optional<Author>
    fun findAllByKeyword(theKeyword: String): List<Author>
    fun save(theAuthor: AuthorRequest): Author
    fun deleteById(theId: Int)
}