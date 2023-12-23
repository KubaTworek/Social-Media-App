package pl.jakubtworek.articles.external

import pl.jakubtworek.common.model.UserDetailsDTO

interface AuthorizationApiService {
    fun getUserDetailsAndValidate(jwt: String, vararg roles: String): UserDetailsDTO
}
