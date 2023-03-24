package com.example.authors.service

import com.example.authors.client.ArticleClient
import com.example.authors.controller.dto.AuthorRequest
import com.example.authors.exception.AuthorNotFoundException
import com.example.authors.model.dto.AuthorDTO
import com.example.authors.model.entity.Author
import com.example.authors.repository.AuthorRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AuthorServiceImpl(
    private val authorRepository: AuthorRepository,
    @Qualifier("ArticleClient") private val articleClient: ArticleClient
) : AuthorService {

    override fun findById(theId: Int): AuthorDTO {
        val author = authorRepository.findByIdOrNull(theId)
            ?: throw AuthorNotFoundException("Author not found")

        return mapAuthorToDTO(author)
    }

    override fun findByUsername(username: String): AuthorDTO {
        val author = authorRepository.findAuthorByUsername(username)
            ?: throw AuthorNotFoundException("Author not found")

        return mapAuthorToDTO(author)
    }

    override fun save(theAuthor: AuthorRequest) {
        val author = createAuthor(theAuthor)

        authorRepository.save(author)
    }

    override fun deleteById(theId: Int) {
        authorRepository.deleteById(theId)
        articleClient.deleteArticlesByAuthorId(theId)
    }

    private fun createAuthor(authorRequest: AuthorRequest): Author =
        Author(
            id = 0,
            firstName = authorRequest.firstName,
            lastName = authorRequest.lastName,
            username = authorRequest.username
        )

    private fun mapAuthorToDTO(author: Author): AuthorDTO =
        AuthorDTO(
            id = author.id,
            firstName = author.firstName,
            lastName = author.lastName,
            username = author.username
        )
}
