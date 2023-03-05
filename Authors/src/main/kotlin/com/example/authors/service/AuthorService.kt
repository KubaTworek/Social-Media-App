package com.example.authors.service

import com.example.authors.controller.AuthorRequest
import com.example.authors.controller.AuthorResponse
import com.example.authors.model.dto.AuthorDTO

interface AuthorService {
    fun findAllAuthors(): List<AuthorResponse>
    fun findById(theId: Int): AuthorDTO
    fun findByUsername(username: String): AuthorDTO
    fun findAllByKeyword(theKeyword: String): List<AuthorResponse>
    fun save(theAuthor: AuthorRequest)
    fun deleteById(theId: Int)
    fun deleteByUsername(username: String)
}