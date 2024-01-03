package pl.jakubtworek.authorization.service

import pl.jakubtworek.authorization.controller.dto.LoginRequest
import pl.jakubtworek.authorization.controller.dto.LoginResponse
import pl.jakubtworek.authorization.controller.dto.RegisterRequest
import pl.jakubtworek.common.model.UserDetailsDTO

interface AuthorizationService {
    fun registerUser(request: RegisterRequest)
    fun deleteUser(jwt: String)
    fun loginUser(request: LoginRequest): LoginResponse
    fun refreshAccessToken(jwt: String): LoginResponse
    fun getUserDetails(jwt: String): UserDetailsDTO
}
