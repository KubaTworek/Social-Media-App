package com.example.articles.service

import com.example.articles.controller.AuthorRequest
import com.example.articles.entity.Author
import java.util.*

interface AuthorService {
    fun findAllAuthors(): List<Author>
    fun findById(theId: Int): Optional<Author>
    fun findAllByKeyword(theKeyword: String): List<Author>
    fun save(theAuthor: AuthorRequest)
    fun deleteById(theId: Int)
}