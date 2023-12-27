package pl.jakubtworek.authors.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import pl.jakubtworek.authors.controller.dto.AuthorRequest
import pl.jakubtworek.authors.entity.Author
import pl.jakubtworek.authors.exception.AuthorNotFoundException
import pl.jakubtworek.authors.external.ArticleApiService
import pl.jakubtworek.authors.repository.AuthorRepository
import pl.jakubtworek.common.model.AuthorDTO

@Service
class AuthorServiceImpl(
    private val authorRepository: AuthorRepository,
    private val articleApiService: ArticleApiService
) : AuthorService {

    private val logger: Logger = LoggerFactory.getLogger(AuthorServiceImpl::class.java)

    override fun findAll(): List<AuthorDTO> {
        logger.info("Fetching all authors")
        return authorRepository.findAll()
            .map { author -> mapAuthorToDTO(author) }
    }

    override fun findById(theId: Int): AuthorDTO {
        logger.info("Fetching author by ID: $theId")
        return authorRepository.findById(theId)
            .map { author -> mapAuthorToDTO(author) }
            .orElseThrow { AuthorNotFoundException("Article not found") }
    }

    override fun findByUsername(username: String): AuthorDTO {
        logger.info("Fetching author by username: $username")
        return authorRepository.findAuthorByUsername(username)
            .map { author -> mapAuthorToDTO(author) }
            .orElseThrow { AuthorNotFoundException("Article not found") }
    }

    override fun save(theAuthor: AuthorRequest) {
        logger.info("Saving author: $theAuthor")
        val author = createAuthor(theAuthor)
        authorRepository.save(author)
        logger.info("Author saved successfully: $author")
    }

    override fun deleteById(theId: Int) {
        logger.info("Deleting author by ID: $theId")
        authorRepository.deleteById(theId)
        articleApiService.deleteArticlesByAuthorId(theId)
        logger.info("Author deleted successfully: ID $theId")
    }

    private fun createAuthor(authorRequest: AuthorRequest): Author = Author(
        id = 0,
        firstName = authorRequest.firstName,
        lastName = authorRequest.lastName,
        username = authorRequest.username
    )

    private fun mapAuthorToDTO(author: Author): AuthorDTO = AuthorDTO(
        id = author.id,
        firstName = author.firstName,
        lastName = author.lastName,
        username = author.username
    )
}
