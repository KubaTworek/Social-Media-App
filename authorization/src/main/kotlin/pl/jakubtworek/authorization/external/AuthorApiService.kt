package pl.jakubtworek.authorization.external

import pl.jakubtworek.common.model.AuthorDTO
import pl.jakubtworek.common.model.AuthorRequest

interface AuthorApiService {
    fun getAuthorByUsername(username: String): AuthorDTO
    fun createAuthor(authorRequest: AuthorRequest)
    fun deleteAuthorById(id: Int)
}