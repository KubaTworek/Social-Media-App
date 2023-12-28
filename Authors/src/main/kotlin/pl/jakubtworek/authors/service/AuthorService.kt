package pl.jakubtworek.authors.service

import pl.jakubtworek.authors.controller.dto.AuthorRequest
import pl.jakubtworek.common.model.AuthorDTO

interface AuthorService {
    fun findAll(): List<AuthorDTO>
    fun findById(theId: Int): AuthorDTO
    fun findByUsername(username: String): AuthorDTO
    fun save(theAuthor: AuthorRequest)
    fun deleteById(theId: Int)
    fun follow(followingId: Int, jwt: String)
    fun unfollow(followingId: Int, jwt: String)
}
