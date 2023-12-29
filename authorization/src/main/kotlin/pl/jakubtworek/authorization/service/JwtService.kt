package pl.jakubtworek.authorization.service

import io.jsonwebtoken.Claims
import pl.jakubtworek.authorization.entity.User

interface JwtService {
    fun buildJwt(user: User, expirationDate: Long): String
    fun parseJwtClaims(jwt: String): Claims
}
