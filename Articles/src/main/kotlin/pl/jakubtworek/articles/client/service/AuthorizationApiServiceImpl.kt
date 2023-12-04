package pl.jakubtworek.articles.client.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import pl.jakubtworek.articles.client.AuthorizationClient
import pl.jakubtworek.articles.model.dto.UserDetailsDTO

@Service
class AuthorizationApiServiceImpl(
    @Qualifier("AuthorizationClient") private val authorizationClient: AuthorizationClient,
    private val objectMapper: ObjectMapper
) : AuthorizationApiService {

    override fun getUserDetails(jwt: String): UserDetailsDTO {
        val response = authorizationClient.getUserDetails(jwt)
        return deserializeUserDetails(response)
    }

    private fun deserializeUserDetails(response: ResponseEntity<String>): UserDetailsDTO =
        objectMapper.readValue(response.body, UserDetailsDTO::class.java)
}
