package pl.jakubtworek.authorization.service

import io.jsonwebtoken.Claims
import pl.jakubtworek.authorization.entity.Authorities

interface JwtService {
    fun buildJwt(username: String, authorities: List<Authorities>, expirationDate: Long): String
    fun parseJwtClaims(jwt: String): Claims
}