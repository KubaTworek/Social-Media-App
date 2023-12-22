package pl.jakubtworek.authorization.service

import pl.jakubtworek.authorization.external.AuthorApiService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import pl.jakubtworek.authorization.controller.dto.LoginRequest
import pl.jakubtworek.authorization.controller.dto.LoginResponse
import pl.jakubtworek.authorization.controller.dto.RegisterRequest
import pl.jakubtworek.authorization.entity.User
import pl.jakubtworek.authorization.exception.UserNotFoundException
import pl.jakubtworek.authorization.exception.UsernameAlreadyExistsException
import pl.jakubtworek.authorization.exception.WrongCredentialsException
import pl.jakubtworek.authorization.repository.UserRepository
import pl.jakubtworek.common.model.AuthorRequest
import pl.jakubtworek.common.model.UserDetailsDTO
import java.time.Instant

@Service
class AuthorizationServiceImpl(
    private val userRepository: UserRepository,
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
        val newUser = buildUser(username, password, role)
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


        val expirationDate = Instant.now().toEpochMilli() + 180000
        val token = jwtService.buildJwt(user, expirationDate)
        val author = authorApiService.getAuthorByUsername(username)

        return LoginResponse(
            username = username,
            firstName = author.firstName,
            lastName = author.lastName,
            token = token,
            role = user.role,
            tokenExpirationDate = expirationDate
        )
    }

    override fun getUserDetails(jwt: String): UserDetailsDTO {
        val claims = jwtService.parseJwtClaims(jwt)
        val username = claims["username"].toString()
        val role = claims["role"].toString()
        val author = authorApiService.getAuthorByUsername(username)

        return UserDetailsDTO(
            authorId = author.id,
            firstName = author.firstName,
            lastName = author.lastName,
            username = username,
            role = role
        )
    }

    private fun buildUser(username: String, password: String, role: String) =
        User(
            username,
            password,
            role
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
}
