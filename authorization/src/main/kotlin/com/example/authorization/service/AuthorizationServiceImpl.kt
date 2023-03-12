package com.example.authorization.service

import com.example.authorization.client.AuthorClient
import com.example.authorization.constants.SecurityConstants.JWT_EXPIRE_TIME
import com.example.authorization.constants.SecurityConstants.JWT_KEY
import com.example.authorization.controller.*
import com.example.authorization.entity.*
import com.example.authorization.exception.*
import com.example.authorization.repository.AuthoritiesRepository
import com.example.authorization.repository.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.SecretKey

@Service
class AuthorizationServiceImpl(
    private val userRepository: UserRepository,
    private val authoritiesRepository: AuthoritiesRepository,
    @Qualifier("AuthorClient") private val authorClient: AuthorClient,
    private val objectMapper: ObjectMapper
) : AuthorizationService {
    override fun registerUser(registerRequest: RegisterRequest) {
        val username = registerRequest.username
        val password = registerRequest.password
        val firstName = registerRequest.firstName
        val lastName = registerRequest.lastName
        val role = registerRequest.role

        validateUserWithThatUsernameDoesNotExist(username)
        val authority = getAuthority(role)
        val newUser = buildUser(username, password, authority)
        userRepository.save(newUser)
        val authorRequest = AuthorRequest(
            firstName,
            lastName,
            username
        )
        authorClient.createAuthor(authorRequest)
    }

    @Transactional
    override fun deleteUser(jwt: String) {
        val claims = parseJwtClaims(jwt)
        val username = claims["username"].toString()
        userRepository.deleteByUsername(username)
        authorClient.deleteAuthorByUsername(username)
    }

    override fun loginUser(loginRequest: LoginRequest): LoginResponse {
        val username = loginRequest.username
        val password = loginRequest.password
        val user = getUserByUsername(username)
        validPasswords(password, user.password)

        val authority = Authorities()
        authority.authority = user.authorities.authority
        val authorities = listOf(
            authority
        )
        val key = createSecretKey()

        return LoginResponse(buildJwt(username, authorities, key))
    }

    override fun getUserDetails(jwt: String): UserResponse {
        val claims = parseJwtClaims(jwt)
        val username = claims["username"].toString()
        val authorities = claims["authorities"].toString()
        val author = deserializeAuthor(getAuthorByUsername(username))

        return UserResponse(author.firstName, author.lastName, username, authorities)
    }

    private fun buildUser(username: String, password: String, authority: Authorities) =
        User(
            username,
            password,
            authority
        )

    private fun buildJwt(username: String, authorities: List<Authorities>, key: SecretKey) =
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
        if (passwordProvided != passwordRegistered) {
            throw WrongCredentialsException("Invalid password!")
        }
    }

    private fun validateUserWithThatUsernameDoesNotExist(username: String) {
        if (userRepository.existsByUsername(username)) {
            throw UsernameAlreadyExistsException("User with this username already exists!")
        }
    }

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

    private fun populateAuthorities(collection: Collection<Authorities>) =
        collection.joinToString(separator = ",") { it.authority }

    private fun getAuthorByUsername(username: String): ResponseEntity<String> =
        authorClient.getAuthorByUsername(username)

    private fun deserializeAuthor(response: ResponseEntity<String>): AuthorDTO =
        objectMapper.readValue(response.body, AuthorDTO::class.java)
}
