package pl.jakubtworek.notifications.client.service

import pl.jakubtworek.notifications.model.dto.AuthorDTO

interface AuthorApiService {
    fun getAuthorById(authorId: Int): AuthorDTO
}
