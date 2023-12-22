package pl.jakubtworek.notifications.external

import pl.jakubtworek.common.model.AuthorDTO

interface AuthorApiService {
    fun getAuthorById(authorId: Int): AuthorDTO
}
