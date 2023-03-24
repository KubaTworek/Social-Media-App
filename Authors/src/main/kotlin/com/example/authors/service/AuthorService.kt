package com.example.authors.service

import com.example.authors.controller.dto.AuthorRequest
import com.example.authors.model.dto.AuthorDTO

interface AuthorService {
    fun findById(theId: Int): AuthorDTO
    fun findByUsername(username: String): AuthorDTO
    fun save(theAuthor: AuthorRequest)
    fun deleteById(theId: Int)
}
