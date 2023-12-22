package pl.jakubtworek.notifications.external

import pl.jakubtworek.common.model.UserDetailsDTO

interface AuthorizationApiService {
    fun getUserDetails(jwt: String): UserDetailsDTO
}
