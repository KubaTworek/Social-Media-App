package pl.jakubtworek.notifications.client.service

import pl.jakubtworek.notifications.model.dto.UserDetailsDTO

interface AuthorizationApiService {
    fun getUserDetails(jwt: String): UserDetailsDTO
}
