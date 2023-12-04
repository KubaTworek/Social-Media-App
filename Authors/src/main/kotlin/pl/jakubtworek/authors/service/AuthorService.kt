package pl.jakubtworek.authors.service

import pl.jakubtworek.authors.controller.dto.AuthorRequest
import pl.jakubtworek.authors.model.dto.AuthorDTO

interface AuthorService {
    fun findById(theId: Int): AuthorDTO
    fun findByUsername(username: String): AuthorDTO
    fun save(theAuthor: AuthorRequest)
    fun deleteById(theId: Int)
}
