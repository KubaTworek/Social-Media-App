package com.example.authorization.service

import com.example.authorization.entity.Authorities
import io.jsonwebtoken.Claims

interface JwtService {
    fun buildJwt(username: String, authorities: List<Authorities>, expirationDate: Long): String
    fun parseJwtClaims(jwt: String): Claims
}