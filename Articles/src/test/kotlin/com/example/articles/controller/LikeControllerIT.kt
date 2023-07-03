package com.example.articles.controller

import com.example.articles.AbstractIT
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LikeControllerIT : AbstractIT() {

    @Test
    fun testLikeArticle() {
        // like one article
        val articles = getArticles(2)
        val articleId = articles.returnResult().responseBody?.firstOrNull()?.id
        likeArticle(articleId!!)

        // get liked article
        val articles2 = getArticles(2)
        val article = articles2.returnResult().responseBody?.filter { it.id == articleId }?.get(0)
        assertEquals(1, article?.numOfLikes)

        // like one more time
        likeArticle(articleId)

        // dislike
        val articles3 = getArticles(2)
        val article2 = articles3.returnResult().responseBody?.filter { it.id == articleId }?.get(0)
        assertEquals(0, article2?.numOfLikes)
    }
}