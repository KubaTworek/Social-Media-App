package pl.jakubtworek.notifications.external

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import pl.jakubtworek.common.client.AuthorizationClient
import pl.jakubtworek.common.model.UserDetailsDTO

@Service
class AuthorizationApiServiceImpl(
    private val authorizationClient: AuthorizationClient,
    private val objectMapper: ObjectMapper
) : AuthorizationApiService {

    override fun getUserDetails(jwt: String): UserDetailsDTO {
        val response = authorizationClient.getUserDetails(jwt)
        return deserializeUserDetails(response)
    }

    private fun deserializeUserDetails(response: ResponseEntity<String>): UserDetailsDTO =
        objectMapper.readValue(response.body, UserDetailsDTO::class.java)
}
