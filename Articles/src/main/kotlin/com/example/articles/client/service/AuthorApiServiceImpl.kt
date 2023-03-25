package com.example.articles.client.service

import com.example.articles.client.AuthorClient
import com.example.articles.model.dto.AuthorDTO
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class AuthorApiServiceImpl(
    @Qualifier("AuthorClient") private val authorClient: AuthorClient,
    private val objectMapper: ObjectMapper
) : AuthorApiService {

    override fun getAuthorById(authorId: Int): AuthorDTO {
        val response = authorClient.getAuthorById(authorId)
        return deserializeAuthor(response)
    }

    private fun deserializeAuthor(response: ResponseEntity<String>): AuthorDTO =
        objectMapper.readValue(response.body, AuthorDTO::class.java)
}
