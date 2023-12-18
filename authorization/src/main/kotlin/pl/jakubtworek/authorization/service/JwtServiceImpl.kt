package pl.jakubtworek.authorization.service

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import pl.jakubtworek.authorization.constants.SecurityConstants
import pl.jakubtworek.authorization.entity.User
import java.nio.charset.StandardCharsets
import java.util.*

@Service
class JwtServiceImpl : JwtService {

    override fun buildJwt(user: User, expirationDate: Long): String =
        Jwts.builder()
            .setIssuer("Social Media")
            .setSubject("JWT Token")
            .claim("username", user.username)
            .claim("role", user.role)
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

    private fun createSecretKey() = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.toByteArray(StandardCharsets.UTF_8))
}
