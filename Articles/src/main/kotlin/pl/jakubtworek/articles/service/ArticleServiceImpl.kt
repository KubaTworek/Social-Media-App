package pl.jakubtworek.articles.service

import jakarta.transaction.Transactional
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import pl.jakubtworek.articles.client.service.AuthorApiService
import pl.jakubtworek.articles.client.service.AuthorizationApiService
import pl.jakubtworek.articles.controller.dto.ArticleRequest
import pl.jakubtworek.articles.controller.dto.ArticleResponse
import pl.jakubtworek.articles.controller.dto.AuthorResponse
import pl.jakubtworek.articles.controller.dto.LikeInfoResponse
import pl.jakubtworek.articles.exception.ArticleNotFoundException
import pl.jakubtworek.articles.exception.UnauthorizedException
import pl.jakubtworek.articles.model.dto.ArticleDTO
import pl.jakubtworek.articles.model.entity.Article
import pl.jakubtworek.articles.repository.ArticleRepository
import pl.jakubtworek.articles.repository.LikeRepository
import java.sql.Timestamp
import java.time.LocalDate

@Service
class ArticleServiceImpl(
    private val articleRepository: ArticleRepository,
    private val likeRepository: LikeRepository,
    private val authorService: AuthorApiService,
    private val authorizationService: AuthorizationApiService
) : ArticleService {

    override fun findAllOrderByCreatedTimeDesc(page: Int, size: Int): List<ArticleResponse> {
        val pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createAt"))
        val articlePage = articleRepository.findAll(pageRequest)

        return articlePage.content.map { createResponse(it) }
    }

    override fun findById(articleId: Int): ArticleDTO {
        val article = articleRepository.findById(articleId).orElse(null)
            ?: throw ArticleNotFoundException("Article not found")
        return mapArticleToDTO(article)
    }

    override fun save(theArticle: ArticleRequest, jwt: String) {
        val article = createArticle(theArticle, jwt)
        articleRepository.save(article)
    }

    @Transactional
    override fun deleteById(theId: Int, jwt: String) {
        val userDetails = authorizationService.getUserDetails(jwt)
        val article = articleRepository.findById(theId).orElse(null)
            ?: throw ArticleNotFoundException("Article not found")
        if (article.authorId == userDetails.authorId || userDetails.role == "ROLE_ADMIN") {
            likeRepository.deleteAllByArticleId(theId)
            articleRepository.deleteById(theId)
        } else {
            throw UnauthorizedException("You are not authorized to delete this article!")
        }
    }

    override fun findAllByAuthorId(authorId: Int): List<ArticleDTO> =
        articleRepository.findAllByAuthorIdOrderByCreateAt(authorId)
            .map { mapArticleToDTO(it) }

    override fun update(theArticle: ArticleRequest, theId: Int, jwt: String) {
        val userDetails = authorizationService.getUserDetails(jwt)
        val article = articleRepository.findById(theId).orElse(null)
            ?: throw ArticleNotFoundException("Article not found")
        if (article.authorId == userDetails.authorId) {
            val updatedArticle = updateArticle(theArticle, article, jwt)
            articleRepository.save(updatedArticle)
        } else {
            throw UnauthorizedException("You are not authorized to delete this article!")
        }
    }

    override fun deleteByAuthorId(authorId: Int) =
        articleRepository.deleteAllByAuthorId(authorId)

    private fun mapArticleToDTO(article: Article): ArticleDTO =
        ArticleDTO(
            id = article.id,
            timestamp = article.createAt.toString(),
            text = article.content,
            authorId = article.authorId
        )

    private fun createArticle(request: ArticleRequest, jwt: String): Article {
        val userDetails = authorizationService.getUserDetails(jwt)

        return Article(
            id = 0,
            createAt = Timestamp(System.currentTimeMillis()),
            content = request.text,
            authorId = userDetails.authorId
        )
    }

    private fun updateArticle(request: ArticleRequest, articleToUpdate: Article, jwt: String): Article {
        val userDetails = authorizationService.getUserDetails(jwt)

        return Article(
            id = articleToUpdate.id,
            createAt = articleToUpdate.createAt,
            content = request.text,
            authorId = userDetails.authorId
        )
    }

    private fun createResponse(theArticle: Article): ArticleResponse {
        val author = authorService.getAuthorById(theArticle.authorId)
        val likes = likeRepository.findByArticleId(theArticle.id)
        val likerFullNames = mutableListOf<String>()

        for (like in likes) {
            val liker = authorService.getAuthorById(like.authorId)
            val fullName = "${liker.firstName} ${liker.lastName}"
            likerFullNames.add(fullName)
        }

        return ArticleResponse(
            id = theArticle.id,
            text = theArticle.content,
            timestamp = theArticle.createAt,
            author = AuthorResponse(
                username = author.username,
                firstName = author.firstName,
                lastName = author.lastName
            ),
            likes = LikeInfoResponse(
                users = likerFullNames
            )
        )
    }
}