package pl.jakubtworek.articles.client.service

import pl.jakubtworek.articles.model.dto.UserDetailsDTO

interface AuthorizationApiService {
    fun getUserDetails(jwt: String): UserDetailsDTO
}
