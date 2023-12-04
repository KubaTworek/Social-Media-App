package pl.jakubtworek.authorization.service

import pl.jakubtworek.authorization.client.service.AuthorApiService
import io.jsonwebtoken.Jwts
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.mockito.*
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.*
import org.springframework.http.ResponseEntity
import pl.jakubtworek.authorization.controller.dto.AuthorRequest
import pl.jakubtworek.authorization.controller.dto.LoginRequest
import pl.jakubtworek.authorization.controller.dto.RegisterRequest
import pl.jakubtworek.authorization.entity.AuthorDTO
import pl.jakubtworek.authorization.entity.Authorities
import pl.jakubtworek.authorization.entity.User
import pl.jakubtworek.authorization.exception.UserNotFoundException
import pl.jakubtworek.authorization.exception.UsernameAlreadyExistsException
import pl.jakubtworek.authorization.exception.WrongCredentialsException
import pl.jakubtworek.authorization.repository.AuthoritiesRepository
import pl.jakubtworek.authorization.repository.UserRepository
import java.time.Instant

class AuthorizationServiceTest {
    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var authoritiesRepository: AuthoritiesRepository

    @Mock
    private lateinit var authorApiService: AuthorApiService

    private lateinit var jwtService: JwtService

    private lateinit var authorizationService: AuthorizationService

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
        jwtService = JwtServiceImpl()
        authorizationService = AuthorizationServiceImpl(
            userRepository,
            authoritiesRepository,
            authorApiService,
            jwtService)
    }

    @Test
    fun `registerUser should create and save a new user with the provided details`() {
        // Given
        val registerRequest = RegisterRequest("username", "password", "John", "Doe", "ROLE_USER")
        val authority = Authorities(1, "ROLE_USER", mutableListOf())

        `when`(userRepository.existsByUsername(registerRequest.username)).thenReturn(false)
        `when`(authoritiesRepository.findAuthoritiesByAuthority(registerRequest.role)).thenReturn(authority)
        `when`(userRepository.save(any(User::class.java))).thenAnswer { it.arguments[0] as User }

        // When
        authorizationService.registerUser(registerRequest)

        // Then
        verify(userRepository).save(any(User::class.java))
        verify(authorApiService).createAuthor(
            AuthorRequest(registerRequest.firstName, registerRequest.lastName, registerRequest.username)
        )
    }

    @Test
    fun `registerUser should throw UsernameAlreadyExistsException when user with the same username already exists`() {
        // Given
        val registerRequest = RegisterRequest("username", "password", "John", "Doe", "ROLE_USER")

        `when`(userRepository.existsByUsername(registerRequest.username)).thenReturn(true)

        // When & Then
        assertThrows(UsernameAlreadyExistsException::class.java) {
            authorizationService.registerUser(registerRequest)
        }
    }

    @Test
    fun `deleteUser should delete the user and associated author`() {
        // Given
        val jwt = jwtService.buildJwt("username", listOf(Authorities(1, "ROLE_USER", mutableListOf())), Instant.MAX.epochSecond)
        val user = User("username", "password", Authorities(1, "ROLE_USER", mutableListOf()))
        val author = AuthorDTO(1, "John", "Doe", "username")

        `when`(authorApiService.getAuthorByUsername("username")).thenReturn(author)
        `when`(userRepository.deleteByUsername(user.username)).thenAnswer { }
        `when`(authorApiService.deleteAuthorById(1)).thenReturn(ResponseEntity.noContent().build())

        // When
        authorizationService.deleteUser(jwt)

        // Then
        verify(userRepository).deleteByUsername(user.username)
        verify(authorApiService).deleteAuthorById(1)
    }

    @Test
    fun `loginUser should return a LoginResponse with a valid JWT token`() {
        // Given
        val loginRequest = LoginRequest("username", "password")
        val user = User("username", "password", Authorities(1, "ROLE_USER", mutableListOf()))
        val author = AuthorDTO(1, "John", "Doe", "username")

        `when`(userRepository.findUserByUsername(loginRequest.username)).thenReturn(user)
        `when`(authorApiService.getAuthorByUsername("username")).thenReturn(author)

        // When
        val loginResponse = authorizationService.loginUser(loginRequest)

        // Then
        val claims = jwtService.parseJwtClaims(loginResponse.token)
        assertEquals("username", claims["username"].toString())
        assertEquals("ROLE_USER", claims["authorities"].toString())
    }

    @Test
    fun `loginUser should throw WrongCredentialsException when an invalid password is provided`() {
        // Given
        val loginRequest = LoginRequest("username", "password")
        val user = User("username", "password123", Authorities(1, "ROLE_USER", mutableListOf()))

        `when`(userRepository.findUserByUsername(loginRequest.username)).thenReturn(user)

        // When & Then
        assertThrows(WrongCredentialsException::class.java) {
            authorizationService.loginUser(loginRequest)
        }
    }

    @Test
    fun `getUserDetails should return UserDetailsDTO with user details extracted from the JWT token`() {
        // Given
        val jwt = jwtService.buildJwt("username", listOf(Authorities(1, "ROLE_USER", mutableListOf())), Instant.MAX.epochSecond)
        val claims = Jwts.claims()
        claims["username"] = "username"
        claims["authorities"] = "ROLE_USER"
        val author = AuthorDTO(1, "John", "Doe", "username")

        `when`(authorApiService.getAuthorByUsername("username")).thenReturn(author)

        // When
        val userDetails = authorizationService.getUserDetails(jwt)

        // Then
        assertEquals(1, userDetails.authorId)
        assertEquals("John", userDetails.firstName)
        assertEquals("Doe", userDetails.lastName)
        assertEquals("username", userDetails.username)
        assertEquals("ROLE_USER", userDetails.role)
    }

    @Test
    fun `getUserDetails should throw UserNotFoundException when user with the given username does not exist`() {
        // Given
        val jwt = jwtService.buildJwt("nonexistentuser", listOf(Authorities(1, "ROLE_USER", mutableListOf())), Instant.MAX.epochSecond)

        `when`(authorApiService.getAuthorByUsername("nonexistentuser")).thenThrow(UserNotFoundException::class.java)

        // When & Then
        assertThrows(UserNotFoundException::class.java) {
            authorizationService.getUserDetails(jwt)
        }
    }
}
