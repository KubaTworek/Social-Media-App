package com.example.authorization.filter

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.nio.charset.StandardCharsets

@RequiredArgsConstructor
class JWTTokenValidatorFilter(
    private val jwtHeader: String,
    private val jwtKey: String
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val jwt = request.getHeader(jwtHeader)
        if (jwt != null) {
            val claims = parseJwt(jwt)
            val username = claims["username"].toString()
            val authorities = claims["authorities"].toString()
            val auth = createAuthentication(username, authorities)
            SecurityContextHolder.getContext().authentication = auth
        }
        chain.doFilter(request, response)
    }

    private fun parseJwt(jwt: String): Claims =
        Jwts.parserBuilder()
            .setSigningKey(jwtKey.toByteArray(StandardCharsets.UTF_8))
            .build()
            .parseClaimsJws(jwt)
            .body

    private fun createAuthentication(username: String, authorities: String): Authentication {
        val authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities)
        return UsernamePasswordAuthenticationToken(username, null, authorityList)
    }
}
