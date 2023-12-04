package pl.jakubtworek.authorization.client.service

import org.springframework.http.ResponseEntity
import pl.jakubtworek.authorization.controller.dto.AuthorRequest
import pl.jakubtworek.authorization.entity.AuthorDTO

interface AuthorApiService {
    fun getAuthorByUsername(username: String): AuthorDTO
    fun createAuthor(authorRequest: AuthorRequest): ResponseEntity<Void>
    fun deleteAuthorById(id: Int): ResponseEntity<Void>
}