package com.example.articles.service

import com.example.articles.kafka.message.LikeMessage
import com.example.articles.kafka.service.KafkaLikeService
import com.example.articles.model.entity.Like
import com.example.articles.repository.LikeRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import java.sql.Timestamp

@Service
class LikeServiceImpl(
    private val likeRepository: LikeRepository,
    private val authorizationService: AuthorizationApiService,
    private val kafkaLikeService: KafkaLikeService,
    private val objectMapper: ObjectMapper
) : LikeService{
    override fun like(articleId: Int, jwt: String) {
        val userDetails = authorizationService.getUserDetails(jwt)
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
        kafkaLikeService.sendLikeMessage(message.serialize())
    }

    private fun LikeMessage.serialize(): String =
        let(objectMapper::writeValueAsString)
}
