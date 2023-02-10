package com.example.authors.service

import com.example.authors.controller.AuthorRequest
import com.example.authors.controller.AuthorResponse
import com.example.authors.model.Author
import java.util.*

interface AuthorService {
    fun findAllAuthors(): List<AuthorResponse>
    fun findById(theId: Int): Optional<Author>
    fun findAllByKeyword(theKeyword: String): List<AuthorResponse>
    fun save(theAuthor: AuthorRequest)
    fun deleteById(theId: Int)
}