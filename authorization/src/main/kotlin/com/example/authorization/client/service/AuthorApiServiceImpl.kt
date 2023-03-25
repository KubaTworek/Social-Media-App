package com.example.authorization.client.service

import com.example.authorization.client.AuthorClient
import com.example.authorization.controller.dto.AuthorRequest
import com.example.authorization.entity.AuthorDTO
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class AuthorApiServiceImpl(
    @Qualifier("AuthorClient") private val authorClient: AuthorClient,
    private val objectMapper: ObjectMapper
) : AuthorApiService {
    override fun getAuthorByUsername(username: String): AuthorDTO {
        val response = authorClient.getAuthorByUsername(username)

        return deserializeAuthor(response)
    }

    override fun createAuthor(authorRequest: AuthorRequest) =
        authorClient.createAuthor(authorRequest)

    override fun deleteAuthorById(id: Int) =
        authorClient.deleteAuthorById(id)

    private fun deserializeAuthor(response: ResponseEntity<String>): AuthorDTO =
        objectMapper.readValue(response.body, AuthorDTO::class.java)
}