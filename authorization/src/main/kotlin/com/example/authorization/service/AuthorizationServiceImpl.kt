package com.example.authorization.service

import com.example.authorization.constants.SecurityConstants.JWT_EXPIRE_TIME
import com.example.authorization.constants.SecurityConstants.JWT_KEY
import com.example.authorization.controller.*
import com.example.authorization.entity.Authorities
import com.example.authorization.entity.User
import com.example.authorization.exception.*
import com.example.authorization.repository.AuthoritiesRepository
import com.example.authorization.repository.UserRepository
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.SecretKey

@Service
class AuthorizationServiceImpl(
    private val userRepository: UserRepository,
    private val authoritiesRepository: AuthoritiesRepository,
    private val passwordEncoder: PasswordEncoder,
) : AuthorizationService {
    override fun registerUser(registerRequest: RegisterRequest): UserResponse {
        val username = registerRequest.username
        val password = registerRequest.password
        val role = registerRequest.role

        validateUserWithThatUsernameDoesNotExist(username)
        val authority = getAuthority(role)
        val newUser = buildUser(username, password, authority)
        val createdUser = userRepository.save(newUser)

        return UserResponse(
            createdUser.username,
            createdUser.authorities.authority
        )
    }

    override fun loginUser(loginRequest: LoginRequest): String {
        val username = loginRequest.username
        val password = loginRequest.password
        val user = getUserByUsername(username)
        validPasswords(password, user.password)

        val authorities = listOf(
            SimpleGrantedAuthority(user.authorities.authority)
        )
        val key = createSecretKey()

        return buildJwt(username, authorities, key)
    }

    override fun getUserDetails(jwt: String): UserResponse {
        val claims = parseJwtClaims(jwt)
        val username = claims["username"].toString()
        val authorities = claims["authorities"].toString()
        return UserResponse(username, authorities)
    }

    private fun buildUser(username: String, password: String, authority: Authorities) =
        User(
            username,
            passwordEncoder.encode(password),
            authority
        )

    private fun buildJwt(username: String, authorities: List<GrantedAuthority>, key: SecretKey) =
        Jwts.builder()
            .setIssuer("Social Media")
            .setSubject("JWT Token")
            .claim("username", username)
            .claim("authorities", populateAuthorities(authorities))
            .setIssuedAt(Date())
            .setExpiration(Date(JWT_EXPIRE_TIME))
            .signWith(key)
            .compact()

    private fun createSecretKey() = Keys.hmacShaKeyFor(JWT_KEY.toByteArray(StandardCharsets.UTF_8))

    private fun validPasswords(passwordProvided: String, passwordRegistered: String) {
        if (!isValidPasswords(passwordProvided, passwordRegistered)) {
            throw WrongCredentialsException("Invalid password!")
        }
    }

    private fun validateUserWithThatUsernameDoesNotExist(username: String) {
        if (userRepository.existsByUsername(username)) {
            throw UsernameAlreadyExistsException("User with this username already exists!")
        }
    }

    private fun isValidPasswords(passwordProvided: String, passwordRegistered: String) =
        passwordEncoder.matches(passwordProvided, passwordRegistered)

    private fun getUserByUsername(username: String) =
        userRepository.findUserByUsername(username)
            .orElseThrow { UserNotFoundException("No user registered with this username!") }

    private fun getAuthority(authority: String) =
        authoritiesRepository.findAuthoritiesByAuthority(authority)
            .orElse(
                authoritiesRepository.findAuthoritiesByAuthority("ROLE_USER")
                    .orElseThrow { RuntimeException("No authorities found!") }
            )

    private fun parseJwtClaims(jwt: String): Claims {
        val key = Keys.hmacShaKeyFor(JWT_KEY.toByteArray(StandardCharsets.UTF_8))
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(jwt)
            .body
    }

    private fun populateAuthorities(collection: Collection<GrantedAuthority>) =
        collection.joinToString(separator = ",") { it.authority }
}
