package com.example.articles.controller

import com.example.articles.AbstractIT
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LikeControllerIT : AbstractIT() {

    @Test
    fun `testLikeArticle should perform like and dislike operations on an article`() {
        // Like one article
        val articlesResponse = getArticles(2)
        val articleId = articlesResponse.returnResult().responseBody?.firstOrNull()?.id
        val likeResponse1 = likeArticle(articleId!!)
        assertEquals("like", likeResponse1.returnResult().responseBody?.status)

        // Get liked article
        val articlesResponse2 = getArticles(2)
        val likedArticle = articlesResponse2.returnResult().responseBody?.find { it.id == articleId }
        assertEquals(1, likedArticle?.numOfLikes)

        // Get info about likes
        val likeInfoResponse = showLikeInfo(articleId)
        val likedUsers = likeInfoResponse.returnResult().responseBody?.users
        assertEquals("FirstName LastName", likedUsers?.first())

        // Like one more time (to dislike)
        val likeResponse2 = likeArticle(articleId)
        assertEquals("dislike", likeResponse2.returnResult().responseBody?.status)

        // Dislike
        val articlesResponse3 = getArticles(2)
        val dislikedArticle = articlesResponse3.returnResult().responseBody?.find { it.id == articleId }
        assertEquals(0, dislikedArticle?.numOfLikes)
    }
}