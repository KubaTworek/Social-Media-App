package pl.jakubtworek.authorization.service

import jakarta.transaction.Transactional
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import pl.jakubtworek.authorization.constants.SecurityConstants.JWT_EXPIRE_TIME
import pl.jakubtworek.authorization.controller.dto.LoginRequest
import pl.jakubtworek.authorization.controller.dto.LoginResponse
import pl.jakubtworek.authorization.controller.dto.RegisterRequest
import pl.jakubtworek.authorization.entity.User
import pl.jakubtworek.authorization.exception.UserNotFoundException
import pl.jakubtworek.authorization.exception.UsernameAlreadyExistsException
import pl.jakubtworek.authorization.exception.WrongCredentialsException
import pl.jakubtworek.authorization.external.AuthorApiService
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

    private val logger: Logger = LoggerFactory.getLogger(AuthorizationServiceImpl::class.java)

    override fun registerUser(registerRequest: RegisterRequest) {
        logger.info("Starting user registration process")
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

        logger.info("User registered successfully: $username")
    }

    override fun loginUser(loginRequest: LoginRequest): LoginResponse {
        logger.info("Starting user login process")
        val username = loginRequest.username
        val password = loginRequest.password
        val user = getUserByUsername(username)
        validPasswords(password, user.password)

        val expirationDate = Instant.now().toEpochMilli() + JWT_EXPIRE_TIME
        val token = jwtService.buildJwt(user, expirationDate)
        val author = authorApiService.getAuthorByUsername(username)

        logger.info("User logged in successfully: $username")
        return LoginResponse(
            username = username,
            firstName = author.firstName,
            lastName = author.lastName,
            following = author.following.size,
            followers = author.followers.size,
            token = token,
            role = user.role,
            tokenExpirationDate = expirationDate
        )
    }

    override fun getUserDetails(jwt: String): UserDetailsDTO {
        logger.info("Starting get user details process")
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

    @Transactional
    override fun deleteUser(jwt: String) {
        logger.info("Starting delete user process")
        val user = getUserDetails(jwt)
        userRepository.deleteByUsername(user.username)
        authorApiService.deleteAuthorById(user.authorId)
        logger.info("User deleted successfully: ${user.username}")
    }

    private fun buildUser(
        username: String,
        password: String,
        role: String
    ): User = User(
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

    private fun getUserByUsername(username: String): User =
        userRepository.findUserByUsername(username)
            .orElseThrow { UserNotFoundException("User not found!") }
}
