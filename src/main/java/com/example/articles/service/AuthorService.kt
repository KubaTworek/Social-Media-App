package com.example.articles.service

import com.example.articles.controller.author.AuthorRequest
import com.example.articles.entity.AuthorPost
import java.util.*

interface AuthorService {
    fun findAllAuthors(): List<AuthorPost>
    fun findById(theId: Int): Optional<AuthorPost>
    fun findAllByKeyword(theKeyword: String): List<AuthorPost>
    fun save(theAuthor: AuthorRequest)
    fun deleteById(theId: Int)
}