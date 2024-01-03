package pl.jakubtworek.authorization.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import pl.jakubtworek.authorization.constants.SecurityConstants
import pl.jakubtworek.authorization.entity.User
import java.nio.charset.StandardCharsets
import java.util.*

@Service
class JwtServiceImpl : JwtService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun buildJwt(user: User, expirationDate: Long): String {
        logger.info("Building token for user: ${user.username}")
        try {
            return Jwts.builder()
                .setIssuer("Social Media")
                .setSubject("JWT Token")
                .claim("username", user.username)
                .claim("role", user.role)
                .setIssuedAt(Date())
                .setExpiration(Date(expirationDate))
                .signWith(createSecretKey())
                .compact()
        } catch (e: Exception) {
            logger.error("Error building refresh token", e)
            throw e
        }
    }

    override fun parseJwtClaims(jwt: String): Claims {
        logger.info("Parsing JWT claims")
        try {
            val jwtWithoutBearer = jwt.replaceFirst("Bearer ", "")
            return Jwts.parserBuilder()
                .setSigningKey(createSecretKey())
                .build()
                .parseClaimsJws(jwtWithoutBearer)
                .body
        } catch (e: ExpiredJwtException) {
            logger.error("Expired JWT token", e)
            throw e
        } catch (e: Exception) {
            logger.error("Error parsing JWT claims", e)
            throw e
        }
    }

    private fun createSecretKey() = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.toByteArray(StandardCharsets.UTF_8))
}
