package com.example.articles.service

import com.example.articles.model.entity.Like
import com.example.articles.repository.LikeRepository
import org.springframework.stereotype.Service
import java.sql.Timestamp

@Service
class LikeServiceImpl(
    private val likeRepository: LikeRepository,
    private val authorizationService: AuthorizationApiService
) : LikeService{
    override fun like(articleId: Int, jwt: String) {
        val userDetails = authorizationService.getUserDetails(jwt)
        val like = Like(
            0,
            Timestamp(System.currentTimeMillis()),
            userDetails.authorId,
            articleId
        )
        likeRepository.save(like)
    }
}