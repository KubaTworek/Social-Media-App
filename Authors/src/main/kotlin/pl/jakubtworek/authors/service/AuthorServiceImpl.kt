package pl.jakubtworek.authors.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import pl.jakubtworek.authors.controller.dto.AuthorRequest
import pl.jakubtworek.authors.entity.Author
import pl.jakubtworek.authors.entity.Follow
import pl.jakubtworek.authors.exception.AuthorNotFoundException
import pl.jakubtworek.authors.external.ArticleApiService
import pl.jakubtworek.authors.external.AuthorizationApiService
import pl.jakubtworek.authors.repository.AuthorRepository
import pl.jakubtworek.authors.repository.FollowRepository
import pl.jakubtworek.common.Constants.ROLE_ADMIN
import pl.jakubtworek.common.Constants.ROLE_USER
import pl.jakubtworek.common.model.AuthorDTO
import java.sql.Timestamp
import java.time.Instant

@Service
class AuthorServiceImpl(
    private val authorRepository: AuthorRepository,
    private val followRepository: FollowRepository,
    private val articleApiService: ArticleApiService,
    private val authorizationService: AuthorizationApiService
) : AuthorService {

    private val logger: Logger = LoggerFactory.getLogger(AuthorServiceImpl::class.java)

    override fun findAll(jwt: String): List<AuthorDTO> {
        logger.info("Fetching all authors")
        authorizationService.getUserDetailsAndValidate(jwt, ROLE_ADMIN)
        return authorRepository.findAll()
            .map { author -> mapAuthorToDTO(author) }
            .also { logger.info("Successfully fetched all authors") }
    }

    override fun findFollowing(jwt: String): List<AuthorDTO> {
        logger.info("Fetching following authors")
        val userDetails = authorizationService.getUserDetailsAndValidate(jwt, ROLE_USER)
        return authorRepository.findAllFollowing(userDetails.authorId)
            .map { author -> mapAuthorToDTO(author) }
            .also { logger.info("Successfully fetched following authors") }
    }

    override fun findFollowers(jwt: String): List<AuthorDTO> {
        logger.info("Fetching followed authors")
        val userDetails = authorizationService.getUserDetailsAndValidate(jwt, ROLE_USER)
        return authorRepository.findAllFollowers(userDetails.authorId)
            .map { author -> mapAuthorToDTO(author) }
            .also { logger.info("Successfully fetched followed authors") }
    }

    override fun findById(theId: Int): AuthorDTO {
        logger.info("Fetching author by ID: $theId")
        return authorRepository.findById(theId)
            .map { author -> mapAuthorToDTO(author) }
            .orElseThrow { AuthorNotFoundException("Author not found with ID: $theId") }
            .also { logger.info("Successfully fetched author by ID: $theId") }
    }

    override fun findByUsername(username: String): AuthorDTO {
        logger.info("Fetching author by username: $username")
        return authorRepository.findAuthorByUsername(username)
            .map { author -> mapAuthorToDTO(author) }
            .orElseThrow { AuthorNotFoundException("Author not found with username: $username") }
            .also { logger.info("Successfully fetched author by username: $username") }
    }

    override fun save(theAuthor: AuthorRequest) {
        logger.info("Saving author: $theAuthor")
        val author = createAuthor(theAuthor)
        authorRepository.save(author)
        logger.info("Successfully saved author: $author")
    }

    override fun follow(followingId: Int, jwt: String) {
        val userDetails = authorizationService.getUserDetailsAndValidate(jwt, ROLE_USER)

        val followerAuthor = authorRepository.findById(userDetails.authorId)
            .orElseThrow { AuthorNotFoundException("Follower author not found") }

        val followingAuthor = authorRepository.findById(followingId)
            .orElseThrow { AuthorNotFoundException("Following author not found") }

        validateFollowRelationshipNotExist(followerAuthor, followingAuthor)

        val follow = Follow(
            id = 0,
            follower = followerAuthor,
            following = followingAuthor,
            createAt = Timestamp.from(Instant.now())
        )

        followRepository.save(follow)

        logger.info(
            "Successfully created follow relationship. Follower: {}, Following: {}",
            followerAuthor.username, followingAuthor.username
        )
    }

    override fun unfollow(followingId: Int, jwt: String) {
        val userDetails = authorizationService.getUserDetailsAndValidate(jwt, ROLE_USER)

        followRepository.deleteByFollowerIdAndFollowingId(userDetails.authorId, followingId)

        logger.info(
            "Successfully deleted follow relationship. Follower: {}, Following: {}",
            userDetails.username, followingId
        )
    }

    override fun deleteById(theId: Int) {
        logger.info("Deleting author by ID: $theId")
        authorRepository.deleteById(theId)
        articleApiService.deleteArticlesByAuthorId(theId)
        logger.info("Successfully deleted author by ID: $theId")
    }

    private fun validateFollowRelationshipNotExist(follower: Author, following: Author) {
        if (hasFollowRelationship(follower, following) || hasFollowRelationship(following, follower)) {
            throw IllegalStateException("Follow relationship already exists!")
        }
    }

    private fun hasFollowRelationship(author1: Author, author2: Author): Boolean {
        return author1.following.any { it.follower == author1 && it.following == author2 }
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
        username = author.username,
        following = author.following.map {it.following.id},
        followers = author.followedBy.map {it.follower.id}
    )
}
