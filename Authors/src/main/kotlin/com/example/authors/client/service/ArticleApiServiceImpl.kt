package com.example.authors.client.service

import com.example.authors.client.ArticleClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class ArticleApiServiceImpl(
    @Qualifier("ArticleClient") private val articleClient: ArticleClient
) : ArticleApiService {
    override fun deleteArticlesByAuthorId(authorId: Int) {
        articleClient.deleteArticlesByAuthorId(authorId)
    }
}