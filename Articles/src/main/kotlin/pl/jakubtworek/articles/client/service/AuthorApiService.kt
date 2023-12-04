package pl.jakubtworek.articles.client.service

import pl.jakubtworek.articles.model.dto.AuthorDTO

interface AuthorApiService {
    fun getAuthorById(authorId: Int): AuthorDTO
}
