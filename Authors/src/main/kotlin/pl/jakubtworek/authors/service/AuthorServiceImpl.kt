package pl.jakubtworek.authors.service

import pl.jakubtworek.authors.client.service.ArticleApiService
import pl.jakubtworek.authors.controller.dto.AuthorRequest
import pl.jakubtworek.authors.exception.AuthorNotFoundException
import pl.jakubtworek.authors.model.dto.AuthorDTO
import pl.jakubtworek.authors.model.entity.Author
import pl.jakubtworek.authors.repository.AuthorRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class AuthorServiceImpl(
    private val authorRepository: AuthorRepository,
    private val articleApiService: ArticleApiService
) : AuthorService {

    override fun findAll(): List<AuthorDTO> {
        return authorRepository.findAll().map { author -> mapAuthorToDTO(author) }.toList()
    }

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
        articleApiService.deleteArticlesByAuthorId(theId)
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
