package com.example.articles.service

import com.example.articles.client.service.AuthorApiService
import com.example.articles.client.service.AuthorizationApiService
import com.example.articles.controller.dto.LikeInfoResponse
import com.example.articles.controller.dto.LikeResponse
import com.example.articles.kafka.message.LikeMessage
import com.example.articles.kafka.service.KafkaLikeService
import com.example.articles.model.entity.Like
import com.example.articles.repository.LikeRepository
import org.springframework.stereotype.Service
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
