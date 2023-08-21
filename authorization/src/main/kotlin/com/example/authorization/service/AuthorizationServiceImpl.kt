package com.example.authorization.service

import com.example.authorization.client.service.AuthorApiService
import com.example.authorization.controller.dto.*
import com.example.authorization.entity.*
import com.example.authorization.exception.*
import com.example.authorization.repository.*
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.time.Instant


@Service
class AuthorizationServiceImpl(
    private val userRepository: UserRepository,
    private val authoritiesRepository: AuthoritiesRepository,
    private val authorApiService: AuthorApiService,
    private val jwtService: JwtService
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
        authorApiService.createAuthor(authorRequest)
    }

    @Transactional
    override fun deleteUser(jwt: String) {
        val user = getUserDetails(jwt)
        userRepository.deleteByUsername(user.username)
        authorApiService.deleteAuthorById(user.authorId)
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

        val expirationDate = Instant.now().toEpochMilli() + 180000
        val token = jwtService.buildJwt(username, authorities, expirationDate)
        val author = authorApiService.getAuthorByUsername(username)

        return LoginResponse(
            username = username,
            firstName = author.firstName,
            lastName = author.lastName,
            token = token,
            tokenExpirationDate = expirationDate
        )
    }

    override fun getUserDetails(jwt: String): UserDetailsDTO {
        val claims = jwtService.parseJwtClaims(jwt)
        val username = claims["username"].toString()
        val authorities = claims["authorities"].toString()
        val author = authorApiService.getAuthorByUsername(username)

        return UserDetailsDTO(
            authorId = author.id,
            firstName = author.firstName,
            lastName = author.lastName,
            username = username,
            role = authorities
        )
    }

    private fun buildUser(username: String, password: String, authority: Authorities) =
        User(
            username,
            password,
            authority
        )

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
            ?: throw UserNotFoundException("No user registered with this username!")

    private fun getAuthority(authority: String) =
        authoritiesRepository.findAuthoritiesByAuthority(authority)
            ?: authoritiesRepository.findAuthoritiesByAuthority("ROLE_USER")
            ?: throw RuntimeException("No authorities found!")
}
