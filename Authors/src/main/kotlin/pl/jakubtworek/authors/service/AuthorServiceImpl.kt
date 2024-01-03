package pl.jakubtworek.authors.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import pl.jakubtworek.authors.controller.dto.AuthorRequest
import pl.jakubtworek.authors.entity.Author
import pl.jakubtworek.authors.entity.Follow
import pl.jakubtworek.authors.exception.AuthorNotFoundException
import pl.jakubtworek.authors.external.ArticleApiService
import pl.jakubtworek.authors.external.AuthorizationApiService
import pl.jakubtworek.authors.kafka.message.FollowMessage
import pl.jakubtworek.authors.kafka.service.KafkaLikeService
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
    private val articleService: ArticleApiService,
    private val authorizationService: AuthorizationApiService,
    private val kafkaLikeService: KafkaLikeService
) : AuthorService {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun getAllAuthors(jwt: String): List<AuthorDTO> {
        logger.info("Fetching all authors")
        authorizationService.getUserDetailsAndValidate(jwt, ROLE_ADMIN)
        return authorRepository.findAll().map { toDTO(it) }.also {
            logger.info("Successfully fetched all authors")
        }
    }

    override fun getFollowing(authorId: Int): List<AuthorDTO> {
        logger.info("Fetching following for author ID: $authorId")
        return authorRepository.findAllFollowing(authorId).map { toDTO(it) }.also {
            logger.info("Successfully fetched following authors")
        }
    }

    override fun getFollowers(authorId: Int): List<AuthorDTO> {
        logger.info("Fetching followers for author ID: $authorId")
        return authorRepository.findAllFollowers(authorId).map { toDTO(it) }.also {
            logger.info("Successfully fetched followed authors")
        }
    }

    override fun getAuthorById(authorId: Int): AuthorDTO {
        logger.info("Fetching author by ID: $authorId")
        return authorRepository.findById(authorId)
            .map { toDTO(it) }
            .orElseThrow { AuthorNotFoundException("Author not found with ID: $authorId") }
            .also { logger.info("Successfully fetched author by ID: $authorId") }
    }

    override fun getAuthorByUsername(username: String): AuthorDTO {
        logger.info("Fetching author by username: $username")
        return authorRepository.findAuthorByUsername(username)
            .map { toDTO(it) }
            .orElseThrow { AuthorNotFoundException("Author not found with username: $username") }
            .also { logger.info("Successfully fetched author by username: $username") }
    }

    override fun saveAuthor(request: AuthorRequest) {
        logger.info("Saving author: $request")
        val author = from(
            request = request
        )
        authorRepository.save(author)
        logger.info("Successfully saved author: $author")
    }

    override fun followAuthor(followingId: Int, jwt: String) {
        val userDetails = authorizationService.getUserDetailsAndValidate(jwt, ROLE_USER)
        val followerAuthor = getAuthorById(userDetails.authorId, "Follower author not found")
        val followingAuthor = getAuthorById(followingId, "Following author not found")
        validateFollowRelationshipNotExist(followerAuthor, followingAuthor)

        val follow = from(
            follower = followerAuthor,
            following = followingAuthor
        )
        followRepository.save(follow)
        sendFollowMessage(followerAuthor.id, followingAuthor.id)

        logger.info(
            "Successfully created follow relationship. Follower: ${followerAuthor.username}, Following: ${followingAuthor.username}"
        )
    }

    override fun unfollowAuthor(followingId: Int, jwt: String) {
        val userDetails = authorizationService.getUserDetailsAndValidate(jwt, ROLE_USER)
        followRepository.deleteByFollowerIdAndFollowingId(userDetails.authorId, followingId)

        logger.info(
            "Successfully deleted follow relationship. Follower: ${userDetails.username}, Following: $followingId"
        )
    }

    override fun deleteAuthorById(authorId: Int) {
        logger.info("Deleting author by ID: $authorId")
        authorRepository.deleteById(authorId)
        articleService.deleteArticlesByAuthorId(authorId)
        logger.info("Successfully deleted author by ID: $authorId")
    }

    private fun getAuthorById(authorId: Int, errorMessage: String): Author =
        authorRepository.findById(authorId)
            .orElseThrow { AuthorNotFoundException(errorMessage) }

    private fun toDTO(author: Author): AuthorDTO = AuthorDTO(
        id = author.id,
        firstName = author.firstName,
        lastName = author.lastName,
        username = author.username,
        following = author.following.map { it.following.id },
        followers = author.followedBy.map { it.follower.id }
    )

    private fun from(follower: Author, following: Author): Follow =
        Follow(
            id = 0,
            follower = follower,
            following = following,
            createAt = Timestamp.from(Instant.now())
        )

    private fun from(request: AuthorRequest): Author =
        Author(
            id = 0,
            firstName = request.firstName,
            lastName = request.lastName,
            username = request.username
        )

    private fun sendFollowMessage(followerId: Int, followedId: Int) {
        val message = FollowMessage(
            timestamp = Timestamp(System.currentTimeMillis()),
            followerId = followerId,
            followedId = followedId
        )
        kafkaLikeService.sendFollowMessage(message)
    }

    private fun isFollow(follower: Author, followed: Author): Boolean =
        follower.following.any { it.following == followed }

    private fun isBeingFollowed(follower: Author, followed: Author): Boolean =
        followed.followedBy.any { it.follower == follower }

    private fun validateFollowRelationshipNotExist(follower: Author, following: Author) {
        if (isFollow(follower, following) || isBeingFollowed(following, follower)) {
            throw IllegalStateException("Follow relationship already exists!")
        }
    }
}
