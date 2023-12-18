package pl.jakubtworek.articles.service

import org.bouncycastle.asn1.x500.style.RFC4519Style.l
import org.springframework.stereotype.Service
import pl.jakubtworek.articles.client.service.AuthorApiService
import pl.jakubtworek.articles.client.service.AuthorizationApiService
import pl.jakubtworek.articles.controller.dto.LikeInfoResponse
import pl.jakubtworek.articles.controller.dto.LikeResponse
import pl.jakubtworek.articles.exception.ArticleNotFoundException
import pl.jakubtworek.articles.kafka.message.LikeMessage
import pl.jakubtworek.articles.kafka.service.KafkaLikeService
import pl.jakubtworek.articles.model.entity.Like
import pl.jakubtworek.articles.repository.ArticleRepository
import pl.jakubtworek.articles.repository.LikeRepository
import java.sql.Timestamp

@Service
class LikeServiceImpl(
    private val likeRepository: LikeRepository,
    private val articleRepository: ArticleRepository,
    private val authorizationService: AuthorizationApiService,
    private val authorApiService: AuthorApiService,
    private val kafkaLikeService: KafkaLikeService
) : LikeService {
    override fun like(articleId: Int, jwt: String): LikeResponse {
        val userDetails = authorizationService.getUserDetails(jwt)
        val article = articleRepository.findById(articleId)

        return if (article.isPresent) {
            val articleEntity = article.get()

            if (articleEntity.likes.any { it.authorId == userDetails.authorId }) {
                val like = articleEntity.likes.first { it.authorId == userDetails.authorId }
                articleEntity.likes.remove(like)
                likeRepository.delete(like)
                LikeResponse("dislike")
            } else {
                val newLike = Like(
                    id = 0,
                    createAt = Timestamp(System.currentTimeMillis()),
                    authorId = userDetails.authorId,
                    article = articleEntity
                )

                articleEntity.likes.add(newLike)
                likeRepository.save(newLike)

                val message = LikeMessage(
                    timestamp = Timestamp(System.currentTimeMillis()),
                    authorId = userDetails.authorId,
                    articleId = articleId
                )
                kafkaLikeService.sendLikeMessage(message)
                LikeResponse("like")
            }
        } else {
            throw ArticleNotFoundException("Article with id $articleId not found.")
        }
    }

    override fun getLikeInfo(articleId: Int): LikeInfoResponse {
        val likes = likeRepository.findByArticleId(articleId)
        val authorNames = likes.map { like ->
            val author = authorApiService.getAuthorById(like.authorId)
            "${author.firstName} ${author.lastName}"
        }
        return LikeInfoResponse(authorNames)
    }
}
