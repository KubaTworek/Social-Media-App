package pl.jakubtworek.authorization.service

import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import pl.jakubtworek.authorization.constants.SecurityConstants.JWT_EXPIRE_TIME
import pl.jakubtworek.authorization.constants.SecurityConstants.REFRESH_TOKEN_EXPIRE_TIME
import pl.jakubtworek.authorization.controller.dto.LoginRequest
import pl.jakubtworek.authorization.controller.dto.LoginResponse
import pl.jakubtworek.authorization.controller.dto.RegisterRequest
import pl.jakubtworek.authorization.entity.User
import pl.jakubtworek.authorization.exception.UserNotFoundException
import pl.jakubtworek.authorization.exception.UsernameAlreadyExistsException
import pl.jakubtworek.authorization.exception.WrongCredentialsException
import pl.jakubtworek.authorization.external.AuthorApiService
import pl.jakubtworek.authorization.repository.UserRepository
import pl.jakubtworek.common.model.AuthorDTO
import pl.jakubtworek.common.model.AuthorRequest
import pl.jakubtworek.common.model.UserDetailsDTO
import java.time.Instant

@Service
class AuthorizationServiceImpl(
    private val userRepository: UserRepository,
    private val authorApiService: AuthorApiService,
    private val jwtService: JwtService,
    private val passwordEncoder: BCryptPasswordEncoder
) : AuthorizationService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun registerUser(request: RegisterRequest) {
        logger.info("Initiating user registration process")
        validateUsernameAvailability(request.username)

        val hashedPassword = passwordEncoder.encode(request.password)
        val newUser = from(
            request = request,
            password = hashedPassword
        )
        userRepository.save(newUser)

        val authorRequest = from(
            request = request
        )
        authorApiService.createAuthor(authorRequest)

        logger.info("User registered successfully: ${request.username}")
    }

    override fun loginUser(request: LoginRequest): LoginResponse {
        logger.info("Initiating user login process")
        val user = getUserByUsername(request.username)
        validatePasswords(request.password, user.password)
        val author = authorApiService.getAuthorByUsername(request.username)

        val tokenExpirationDate = Instant.now().toEpochMilli() + JWT_EXPIRE_TIME
        val refreshTokenExpirationDate = Instant.now().toEpochMilli() + REFRESH_TOKEN_EXPIRE_TIME
        val token = jwtService.buildJwt(user, tokenExpirationDate)
        val refreshToken = jwtService.buildJwt(user, refreshTokenExpirationDate)

        logger.info("User logged in successfully: ${request.username}")
        return createLoginResponse(
            author = author,
            user = user,
            token = token,
            refreshToken = refreshToken,
            tokenExpirationDate = tokenExpirationDate,
            refreshTokenExpirationDate = refreshTokenExpirationDate
        )
    }

    override fun refreshAccessToken(jwt: String): LoginResponse {
        logger.info("Initiating refresh token process")

        val claims = jwtService.parseJwtClaims(jwt)
        val username = claims["username"].toString()
        val user = getUserByUsername(username)
        val author = authorApiService.getAuthorByUsername(username)

        val tokenExpirationDate = Instant.now().toEpochMilli() + JWT_EXPIRE_TIME
        val refreshTokenExpirationDate = Instant.now().toEpochMilli() + REFRESH_TOKEN_EXPIRE_TIME
        val token = jwtService.buildJwt(user, tokenExpirationDate)
        val refreshToken = jwtService.buildJwt(user, refreshTokenExpirationDate)

        logger.info("Token refreshed successfully for: $username")
        return createLoginResponse(
            author = author,
            user = user,
            token = token,
            refreshToken = refreshToken,
            tokenExpirationDate = tokenExpirationDate,
            refreshTokenExpirationDate = refreshTokenExpirationDate
        )
    }

    override fun getUserDetails(jwt: String): UserDetailsDTO {
        logger.info("Initiating get user details process")
        val claims = jwtService.parseJwtClaims(jwt)
        val username = claims["username"].toString()
        val role = claims["role"].toString()
        val author = authorApiService.getAuthorByUsername(username)

        return createUserDetails(
            author = author,
            role = role
        )
    }

    @Transactional
    override fun deleteUser(jwt: String) {
        logger.info("Initiating delete user process")
        val user = getUserDetails(jwt)
        userRepository.deleteByUsername(user.username)
        authorApiService.deleteAuthorById(user.authorId)
        logger.info("User deleted successfully: ${user.username}")
    }

    private fun getUserByUsername(username: String): User =
        userRepository.findUserByUsername(username)
            .orElseThrow { UserNotFoundException("User not found!") }

    private fun from(request: RegisterRequest, password: String): User =
        User(
            username = request.username,
            password = password,
            role = request.role
        )

    private fun from(request: RegisterRequest): AuthorRequest =
        AuthorRequest(
            firstName = request.firstName,
            lastName = request.lastName,
            username = request.username
        )

    private fun createUserDetails(author: AuthorDTO, role: String): UserDetailsDTO =
        UserDetailsDTO(
            authorId = author.id,
            firstName = author.firstName,
            lastName = author.lastName,
            username = author.username,
            role = role
        )

    private fun createLoginResponse(
        author: AuthorDTO,
        user: User,
        token: String,
        refreshToken: String,
        tokenExpirationDate: Long,
        refreshTokenExpirationDate: Long
    ): LoginResponse =
        LoginResponse(
            id = author.id,
            username = user.username,
            firstName = author.firstName,
            lastName = author.lastName,
            following = author.following.size,
            followers = author.followers.size,
            token = token,
            refreshToken = refreshToken,
            role = user.role,
            tokenExpirationDate = tokenExpirationDate,
            refreshTokenExpirationDate = refreshTokenExpirationDate
        )

    private fun validatePasswords(passwordProvided: String, passwordRegistered: String) {
        if (!passwordEncoder.matches(passwordProvided, passwordRegistered)) {
            throw WrongCredentialsException("Invalid password!")
        }
    }

    private fun validateUsernameAvailability(username: String) {
        if (userRepository.existsByUsername(username)) {
            throw UsernameAlreadyExistsException("User with this username already exists!")
        }
    }
}
