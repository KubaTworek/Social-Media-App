package pl.jakubtworek.articles.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import pl.jakubtworek.articles.controller.dto.*
import pl.jakubtworek.articles.entity.Article
import pl.jakubtworek.articles.entity.Like
import pl.jakubtworek.articles.exception.ArticleNotFoundException
import pl.jakubtworek.articles.exception.UnauthorizedException
import pl.jakubtworek.articles.external.AuthorApiService
import pl.jakubtworek.articles.external.AuthorizationApiService
import pl.jakubtworek.articles.kafka.message.LikeMessage
import pl.jakubtworek.articles.kafka.service.KafkaLikeService
import pl.jakubtworek.articles.repository.ArticleRepository
import pl.jakubtworek.common.model.ArticleDTO
import pl.jakubtworek.common.model.UserDetailsDTO
import java.sql.Timestamp

@Service
class ArticleServiceImpl(
    private val articleRepository: ArticleRepository,
    private val authorService: AuthorApiService,
    private val authorizationService: AuthorizationApiService,
    private val kafkaLikeService: KafkaLikeService
) : ArticleService {

    private val logger: Logger = LoggerFactory.getLogger(ArticleServiceImpl::class.java)

    override fun findAllOrderByCreatedTimeDesc(page: Int, size: Int): List<ArticleResponse> {
        logger.info("Finding articles, page: $page, size: $size")
        val pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createAt"))
        val articlePage = articleRepository.findAll(pageRequest)

        val articles = articlePage.content.map { createResponse(it) }
        logger.info("Found ${articles.size} articles.")
        return articles
    }

    override fun findAllByAuthorId(authorId: Int): List<ArticleDTO> {
        logger.info("Finding articles by author ID: $authorId")
        val articles = articleRepository.findAllByAuthorIdOrderByCreateAt(authorId)
            .map { mapArticleToDTO(it) }
        logger.info("Found ${articles.size} articles by author ID: $authorId")
        return articles
    }

    override fun findById(articleId: Int): ArticleDTO {
        logger.info("Finding article by ID: $articleId")
        val article = articleRepository.findById(articleId)
            .orElseThrow { ArticleNotFoundException("Article not found") }
        val articleDTO = mapArticleToDTO(article)
        logger.info("Found article: $articleDTO")
        return articleDTO
    }

    override fun save(theArticle: ArticleRequest, jwt: String) {
        logger.info("Saving article")
        val userDetails = authorizationService.getUserDetailsAndValidate(jwt, ROLE_USER, ROLE_ADMIN)
        val article = createArticle(theArticle, userDetails)
        articleRepository.save(article)
        logger.info("Article saved successfully")
    }

    override fun update(theArticle: ArticleRequest, articleId: Int, jwt: String) {
        logger.info("Updating article by ID: $articleId")
        val userDetails = authorizationService.getUserDetailsAndValidate(jwt, ROLE_USER, ROLE_ADMIN)
        val article = articleRepository.findById(articleId)
            .orElseThrow { ArticleNotFoundException("Article not found") }

        if (article.authorId == userDetails.authorId) {
            val updatedArticle = updateArticle(theArticle, article, userDetails)
            articleRepository.save(updatedArticle)
            logger.info("Article updated successfully")
        } else {
            logger.warn("Unauthorized attempt to update article by user: ${userDetails.username}")
            throw UnauthorizedException("You are not authorized to update this article!")
        }
    }

    override fun like(articleId: Int, jwt: String): LikeResponse {
        logger.info("Processing like for article ID: $articleId")
        val userDetails = authorizationService.getUserDetailsAndValidate(jwt, ROLE_USER)
        val article = articleRepository.findById(articleId)

        return if (article.isPresent) {
            val articleEntity = article.get()

            val hasLiked = articleEntity.likes.any { it.authorId == userDetails.authorId }
            if (hasLiked) {
                val like = articleEntity.likes.first { it.authorId == userDetails.authorId }
                articleEntity.likes.remove(like)
                articleRepository.save(articleEntity)
                logger.info("Removed like for article ID: $articleId by user ID: ${userDetails.authorId}")
                LikeResponse("dislike")
            } else {
                val newLike = Like(
                    id = 0,
                    createAt = Timestamp(System.currentTimeMillis()),
                    authorId = userDetails.authorId,
                    article = articleEntity
                )

                articleEntity.likes.add(newLike)
                articleRepository.save(articleEntity)

                val message = LikeMessage(
                    timestamp = Timestamp(System.currentTimeMillis()),
                    authorId = userDetails.authorId,
                    articleId = articleId
                )
                kafkaLikeService.sendLikeMessage(message)
                logger.info("Added like for article ID: $articleId by user ID: ${userDetails.authorId}")
                LikeResponse("like")
            }
        } else {
            logger.warn("Attempted to like non-existent article with ID: $articleId by user ID: ${userDetails.authorId}")
            throw ArticleNotFoundException("Article with id $articleId not found.")
        }
    }

    override fun deleteById(theId: Int, jwt: String) {
        logger.info("Deleting article by ID: $theId")
        val userDetails = authorizationService.getUserDetailsAndValidate(jwt, ROLE_USER, ROLE_ADMIN)
        val article = articleRepository.findById(theId)
            .orElseThrow { ArticleNotFoundException("Article not found") }

        if (article.authorId == userDetails.authorId || ROLE_ADMIN == userDetails.role) {
            articleRepository.deleteById(theId)
            logger.info("Article deleted successfully")
        } else {
            logger.warn("Unauthorized attempt to delete article by user: ${userDetails.username}")
            throw UnauthorizedException("You are not authorized to delete this article!")
        }
    }

    override fun deleteByAuthorId(authorId: Int) {
        logger.info("Deleting articles by author ID: $authorId")
        articleRepository.deleteAllByAuthorId(authorId)
        logger.info("Articles deleted successfully for author ID: $authorId")
    }

    private fun createResponse(theArticle: Article): ArticleResponse {
        val author = authorService.getAuthorById(theArticle.authorId)
        val likerIds = theArticle.likes.map { it.authorId }

        val likerFullNames = likerIds.map { it ->
            authorService.getAuthorById(it).let { "${it.firstName} ${it.lastName}" }
        }

        return ArticleResponse(
            id = theArticle.id,
            text = theArticle.content,
            timestamp = theArticle.createAt,
            author = author.let {
                AuthorResponse(
                    username = it.username,
                    firstName = it.firstName,
                    lastName = it.lastName
                )
            },
            likes = LikeInfoResponse(
                users = likerFullNames
            )
        )
    }

    private fun mapArticleToDTO(article: Article): ArticleDTO = ArticleDTO(
        id = article.id,
        timestamp = article.createAt.toString(),
        text = article.content,
        authorId = article.authorId
    )

    private fun createArticle(request: ArticleRequest, userDetails: UserDetailsDTO): Article = Article(
        id = 0,
        createAt = Timestamp(System.currentTimeMillis()),
        content = request.text,
        authorId = userDetails.authorId
    )

    private fun updateArticle(request: ArticleRequest, articleToUpdate: Article, userDetails: UserDetailsDTO): Article =
        Article(
            id = articleToUpdate.id,
            createAt = articleToUpdate.createAt,
            content = request.text,
            authorId = userDetails.authorId
        )

    companion object {
        private const val ROLE_USER = "ROLE_USER"
        private const val ROLE_ADMIN = "ROLE_ADMIN"
    }
}
