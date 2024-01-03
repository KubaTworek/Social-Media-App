package pl.jakubtworek.authors.service

import pl.jakubtworek.authors.controller.dto.AuthorRequest
import pl.jakubtworek.common.model.AuthorDTO

interface AuthorService {
    fun getAllAuthors(jwt: String): List<AuthorDTO>
    fun getFollowing(authorId: Int): List<AuthorDTO>
    fun getFollowers(authorId: Int): List<AuthorDTO>
    fun getAuthorById(authorId: Int): AuthorDTO
    fun getAuthorByUsername(username: String): AuthorDTO
    fun saveAuthor(request: AuthorRequest)
    fun followAuthor(followingId: Int, jwt: String)
    fun unfollowAuthor(followingId: Int, jwt: String)
    fun deleteAuthorById(authorId: Int)
}
