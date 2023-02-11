package com.example.authors.service

import com.example.authors.controller.AuthorRequest
import com.example.authors.controller.AuthorResponse
import com.example.authors.factories.AuthorFactory
import com.example.authors.model.dto.AuthorDTO
import com.example.authors.repository.AuthorRepository
import lombok.RequiredArgsConstructor
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
@RequiredArgsConstructor
class AuthorServiceImpl(
    private val authorRepository: AuthorRepository,
    private val authorFactory: AuthorFactory
) : AuthorService {
    override fun findAllAuthors(): List<AuthorResponse> =
        authorRepository.findAll()
            .map { authorFactory.createResponse(it) }

    override fun findById(theId: Int): AuthorDTO =
        authorRepository.findByIdOrNull(theId)?.toDTO()
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)

    override fun findAllByKeyword(theKeyword: String): List<AuthorResponse> =
        authorRepository.findAll()
            .stream()
            .filter { it.firstName.contains(theKeyword) || it.lastName.contains(theKeyword) }
            .map { authorFactory.createResponse(it) }
            .toList()

    override fun save(theAuthor: AuthorRequest) {
        val author = authorFactory.createAuthor(theAuthor)

        authorRepository.save(author)
    }

    override fun deleteById(theId: Int): Unit =
        authorRepository.deleteById(theId)
}