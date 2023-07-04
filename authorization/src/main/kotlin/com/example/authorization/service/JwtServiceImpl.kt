package com.example.authorization.service

import com.example.authorization.constants.SecurityConstants
import com.example.authorization.entity.Authorities
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.util.*

@Service
class JwtServiceImpl : JwtService {

    override fun buildJwt(username: String, authorities: List<Authorities>): String =
        Jwts.builder()
            .setIssuer("Social Media")
            .setSubject("JWT Token")
            .claim("username", username)
            .claim("authorities", populateAuthorities(authorities))
            .setIssuedAt(Date())
            .setExpiration(Date(SecurityConstants.JWT_EXPIRE_TIME))
            .signWith(createSecretKey())
            .compact()

    override fun parseJwtClaims(jwt: String): Claims {
        val key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.toByteArray(StandardCharsets.UTF_8))
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(jwt)
            .body
    }

    private fun populateAuthorities(collection: Collection<Authorities>) =
        collection.joinToString(separator = ",") { it.authority }

    private fun createSecretKey() = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.toByteArray(StandardCharsets.UTF_8))
}
