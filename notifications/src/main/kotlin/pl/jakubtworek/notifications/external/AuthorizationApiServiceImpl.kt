package pl.jakubtworek.notifications.external

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import pl.jakubtworek.common.client.AuthorizationClient
import pl.jakubtworek.common.model.UserDetailsDTO
import pl.jakubtworek.notifications.exception.AuthorizationApiException
import pl.jakubtworek.notifications.exception.UnauthorizedException

@Service
class AuthorizationApiServiceImpl(
    private val authorizationClient: AuthorizationClient,
    private val objectMapper: ObjectMapper
) : AuthorizationApiService {

    private val logger: Logger = LoggerFactory.getLogger(AuthorizationApiServiceImpl::class.java)

    override fun getUserDetailsAndValidate(jwt: String, vararg roles: String): UserDetailsDTO {
        logger.info("Getting user details for JWT: $jwt")
        val response = authorizationClient.getUserDetails(jwt)

        val userDetails = deserializeUserDetails(response)
        if (roles.any { it == userDetails.role }) {
            return userDetails
        }
        throw UnauthorizedException("You are not authorized to do this!")
    }

    private fun deserializeUserDetails(response: ResponseEntity<String>): UserDetailsDTO {
        val responseBody: String = requireNotNull(response.body) { "Authorization API response body is null" }

        require(response.statusCode == HttpStatus.OK) { "Authorization API request failed with status code: ${response.statusCode}" }

        try {
            val userDetailsDTO = objectMapper.readValue(responseBody, UserDetailsDTO::class.java)
            logger.debug("Deserialized user details: $userDetailsDTO")
            return userDetailsDTO
        } catch (e: Exception) {
            logger.error("Error deserializing UserDetailsDTO", e)
            throw AuthorizationApiException("Error deserializing UserDetailsDTO", e)
        }
    }
}
