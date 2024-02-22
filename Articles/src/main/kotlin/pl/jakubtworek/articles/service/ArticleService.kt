package pl.jakubtworek.articles.service

import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import pl.jakubtworek.articles.controller.dto.*
import pl.jakubtworek.articles.entity.Article
import pl.jakubtworek.articles.entity.Like
import pl.jakubtworek.articles.exception.ArticleNotFoundException
import pl.jakubtworek.articles.external.AuthorApiService
import pl.jakubtworek.articles.external.AuthorizationApiService
import pl.jakubtworek.articles.kafka.message.ArticleMessage
import pl.jakubtworek.articles.kafka.message.LikeMessage
import pl.jakubtworek.articles.kafka.service.KafkaLikeService
import pl.jakubtworek.articles.repository.ArticleRepository
import pl.jakubtworek.common.Constants.DISLIKE_ACTION
import pl.jakubtworek.common.Constants.LIKE_ACTION
import pl.jakubtworek.common.Constants.ROLE_ADMIN
import pl.jakubtworek.common.Constants.ROLE_USER
import pl.jakubtworek.common.exception.UnauthorizedException
import pl.jakubtworek.common.model.ArticleDTO
import pl.jakubtworek.common.model.UserDetailsDTO
import java.sql.Timestamp

@Service
class ArticleService(
    private val articleRepository: ArticleRepository,
    private val authorService: AuthorApiService,
    private val authorizationService: AuthorizationApiService,
    private val articleResponseFactory: ArticleResponseFactory,
    private val kafkaLikeService: KafkaLikeService
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    fun saveArticle(request: ArticleCreateRequest, jwt: String): ArticleResponse {
        logger.info("Saving article")
        val userDetails = authorizationService.getUserDetailsAndValidate(jwt, ROLE_USER, ROLE_ADMIN)
        val motherArticle = request.articleMotherId?.let {
            articleRepository.findById(it)
                .orElseThrow { ArticleNotFoundException("Article not found") }
        }
        val article = from(
            request = request,
            userDetails = userDetails,
            motherArticle = motherArticle
        )
        val created = articleRepository.save(article)
        sendArticleMessage(created)
        logger.info("Article saved successfully")
        return articleResponseFactory.createResponse(created, userDetails.authorId)
    }

    fun handleLikeAction(articleId: Int, jwt: String): LikeActionResponse {
        logger.info("Processing like for article ID: $articleId")
        val userDetails = authorizationService.getUserDetailsAndValidate(jwt, ROLE_USER)
        val article = articleRepository.findById(articleId)
            .orElseThrow { ArticleNotFoundException("Article not found") }

        val hasLiked = article.likes.any { it.authorId == userDetails.authorId }
        return if (hasLiked) {
            removeLike(article, userDetails.authorId)
            LikeActionResponse(DISLIKE_ACTION)
        } else {
            addLike(article, userDetails.authorId)
            LikeActionResponse(LIKE_ACTION)
        }
    }

    fun updateArticle(request: ArticleUpdateRequest, jwt: String) {
        logger.info("Updating article by ID: ${request.articleId}")
        val userDetails = authorizationService.getUserDetailsAndValidate(jwt, ROLE_USER, ROLE_ADMIN)
        val article = articleRepository.findById(request.articleId)
            .orElseThrow { ArticleNotFoundException("Article not found") }

        if (canUpdateArticle(article.authorId, userDetails.authorId)) {
            val updated = from(
                request = request,
                articleToUpdate = article,
                userDetails = userDetails
            )
            articleRepository.save(updated)
            logger.info("Article updated successfully")
        } else {
            handleUnauthorizedUpdateAttempt(userDetails.username)
        }
    }

    fun getLatestArticles(page: Int, size: Int, jwt: String): List<ArticleResponse> {
        logger.info("Fetching latest articles, page: $page, size: $size")
        val userDetails = authorizationService.getUserDetailsAndValidate(jwt, ROLE_USER, ROLE_ADMIN)
        val pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createAt"))
        return articleRepository.findAll(pageRequest)
            .content
            .map { articleResponseFactory.createResponse(it, userDetails.authorId) }
    }

    fun getLatestFollowingArticles(page: Int, size: Int, jwt: String): List<ArticleResponse> {
        logger.info("Fetching latest following articles, page: $page, size: $size")
        val userDetails = authorizationService.getUserDetailsAndValidate(jwt, ROLE_USER, ROLE_ADMIN)
        val author = authorService.getAuthorById(userDetails.authorId)
        val pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createAt"))
        return articleRepository.findAllByAuthorIdInOrderByCreateAt(author.following, pageRequest)
            .content
            .map { articleResponseFactory.createResponse(it, userDetails.authorId) }
    }

    fun getArticle(articleId: Int, jwt: String): ArticleDetailsResponse {
        logger.info("Fetching article by ID: $articleId")
        val userDetails = authorizationService.getUserDetailsAndValidate(jwt, ROLE_USER, ROLE_ADMIN)

        val articleWithLikes = articleRepository.findByIdWithLikes(articleId)
            .orElseThrow { ArticleNotFoundException("Article not found") }

        val articleWithArticles = articleRepository.findByIdWithArticles(articleId)
            .orElseThrow { ArticleNotFoundException("Article not found") }

        val response = articleResponseFactory.createResponseForOneArticle(articleWithLikes, userDetails.authorId)

        response.comments = articleWithArticles.articles
            .map { articleResponseFactory.createResponse(it, userDetails.authorId) }

        return response
    }

    fun getArticlesByAuthorId(authorId: Int): List<ArticleDTO> {
        logger.info("Fetching articles by author ID: $authorId")
        return articleRepository.findAllByAuthorIdOrderByCreateAt(authorId)
            .map { toDTO(it) }
    }

    fun getArticleById(articleId: Int): ArticleDTO {
        logger.info("Fetching article by ID: $articleId")
        return articleRepository.findById(articleId)
            .map { toDTO(it) }
            .orElseThrow { ArticleNotFoundException("Article not found") }
    }

    fun deleteArticleById(articleId: Int, jwt: String) {
        logger.info("Deleting article by ID: $articleId")
        val userDetails = authorizationService.getUserDetailsAndValidate(jwt, ROLE_USER, ROLE_ADMIN)
        val article = articleRepository.findById(articleId)
            .orElseThrow { ArticleNotFoundException("Article not found") }

        if (canDeleteArticle(article.authorId, userDetails.authorId, userDetails.role)) {
            articleRepository.deleteById(articleId)
            logger.info("Article deleted successfully")
        } else {
            handleUnauthorizedDeleteAttempt(userDetails.username)
        }
    }

    fun deleteArticlesByAuthorId(authorId: Int) {
        logger.info("Deleting articles by author ID: $authorId")
        articleRepository.deleteAllByAuthorId(authorId)
        logger.info("Articles deleted successfully for author ID: $authorId")
    }

    private fun removeLike(article: Article, userId: Int) {
        val like = article.likes.first { it.authorId == userId }
        article.likes.remove(like)
        articleRepository.save(article)
        logger.info("Removed like for article ID: ${article.id} by user ID: $userId")
    }

    private fun addLike(article: Article, userId: Int) {
        val newLike = from(
            likerId = userId,
            likedArticle = article
        )
        article.likes.add(newLike)
        articleRepository.save(article)

        sendLikeMessage(userId, article.id)
        logger.info("Added like for article ID: ${article.id} by user ID: $userId")
    }

    private fun toDTO(article: Article): ArticleDTO =
        ArticleDTO(
            id = article.id,
            timestamp = article.createAt.toString(),
            text = article.content,
            authorId = article.authorId
        )

    private fun from(request: ArticleCreateRequest, userDetails: UserDetailsDTO, motherArticle: Article?): Article =
        Article(
            id = 0,
            createAt = Timestamp(System.currentTimeMillis()),
            content = request.text,
            authorId = userDetails.authorId,
            motherArticle = motherArticle
        )

    private fun from(likerId: Int, likedArticle: Article): Like =
        Like(
            id = 0,
            createAt = Timestamp(System.currentTimeMillis()),
            authorId = likerId,
            article = likedArticle
        )

    private fun from(request: ArticleUpdateRequest, articleToUpdate: Article, userDetails: UserDetailsDTO): Article =
        Article(
            id = articleToUpdate.id,
            createAt = articleToUpdate.createAt,
            content = request.text,
            authorId = userDetails.authorId
        )

    private fun sendArticleMessage(created: Article) {
        val message = ArticleMessage(
            timestamp = Timestamp(System.currentTimeMillis()),
            articleId = created.id,
            authorId = created.authorId
        )
        kafkaLikeService.sendArticleMessage(message)
    }

    private fun sendLikeMessage(authorId: Int, articleId: Int) {
        val message = LikeMessage(
            timestamp = Timestamp(System.currentTimeMillis()),
            authorId = authorId,
            articleId = articleId
        )
        kafkaLikeService.sendLikeMessage(message)
    }

    private fun canUpdateArticle(articleAuthorId: Int, userId: Int): Boolean =
        articleAuthorId == userId

    private fun canDeleteArticle(articleAuthorId: Int, userId: Int, userRole: String): Boolean =
        articleAuthorId == userId || ROLE_ADMIN == userRole

    private fun handleUnauthorizedUpdateAttempt(username: String): Nothing {
        logger.warn("Unauthorized attempt to update article by user: $username")
        throw UnauthorizedException("You are not authorized to update this article!")
    }

    private fun handleUnauthorizedDeleteAttempt(username: String): Nothing {
        logger.warn("Unauthorized attempt to delete article by user: $username")
        throw UnauthorizedException("You are not authorized to delete this article!")
    }
}
