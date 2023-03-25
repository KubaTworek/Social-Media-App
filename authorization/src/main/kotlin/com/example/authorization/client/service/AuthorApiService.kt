package com.example.authorization.client.service

import com.example.authorization.controller.dto.AuthorRequest
import com.example.authorization.entity.AuthorDTO
import org.springframework.http.ResponseEntity

interface AuthorApiService {
    fun getAuthorByUsername(username: String): AuthorDTO
    fun createAuthor(authorRequest: AuthorRequest): ResponseEntity<Void>
    fun deleteAuthorById(id: Int): ResponseEntity<Void>
}