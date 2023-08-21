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

    override fun buildJwt(username: String, authorities: List<Authorities>, expirationDate: Long): String =
        Jwts.builder()
            .setIssuer("Social Media")
            .setSubject("JWT Token")
            .claim("username", username)
            .claim("authorities", populateAuthorities(authorities))
            .setIssuedAt(Date())
            .setExpiration(Date(expirationDate))
            .signWith(createSecretKey())
            .compact()

    override fun parseJwtClaims(jwt: String): Claims {
        val jwtWithoutBearer = jwt.replaceFirst("Bearer ", "")
        return Jwts.parserBuilder()
            .setSigningKey(createSecretKey())
            .build()
            .parseClaimsJws(jwtWithoutBearer)
            .body
    }

    private fun populateAuthorities(collection: Collection<Authorities>) =
        collection.joinToString(separator = ",") { it.authority }

    private fun createSecretKey() = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.toByteArray(StandardCharsets.UTF_8))
}
