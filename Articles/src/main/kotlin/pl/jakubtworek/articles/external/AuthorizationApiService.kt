package pl.jakubtworek.articles.external

import pl.jakubtworek.common.model.UserDetailsDTO

interface AuthorizationApiService {
    fun getUserDetails(jwt: String): UserDetailsDTO
}
