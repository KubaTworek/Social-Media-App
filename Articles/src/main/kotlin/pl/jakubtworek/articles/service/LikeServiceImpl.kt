package pl.jakubtworek.articles.service

import org.springframework.stereotype.Service
import pl.jakubtworek.articles.client.service.AuthorApiService
import pl.jakubtworek.articles.client.service.AuthorizationApiService
import pl.jakubtworek.articles.controller.dto.LikeInfoResponse
import pl.jakubtworek.articles.controller.dto.LikeResponse
import pl.jakubtworek.articles.kafka.message.LikeMessage
import pl.jakubtworek.articles.kafka.service.KafkaLikeService
import pl.jakubtworek.articles.model.entity.Like
import pl.jakubtworek.articles.repository.LikeRepository
import java.sql.Timestamp

@Service
class LikeServiceImpl(
    private val likeRepository: LikeRepository,
    private val authorizationService: AuthorizationApiService,
    private val authorApiService: AuthorApiService,
    private val kafkaLikeService: KafkaLikeService
) : LikeService {
    override fun like(articleId: Int, jwt: String): LikeResponse {
        val userDetails = authorizationService.getUserDetails(jwt)
        val like = likeRepository.findByArticleIdAndAuthorId(articleId, userDetails.authorId)
        if (like == null) {
            Like(
                id = 0,
                timestamp = Timestamp(System.currentTimeMillis()),
                authorId = userDetails.authorId,
                articleId = articleId
            ).apply { likeRepository.save(this) }

            val message = LikeMessage(
                timestamp = Timestamp(System.currentTimeMillis()),
                authorId = userDetails.authorId,
                articleId = articleId
            )
            kafkaLikeService.sendLikeMessage(message)
            return LikeResponse("like")
        } else {
            likeRepository.delete(like)
            return LikeResponse("dislike")
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
