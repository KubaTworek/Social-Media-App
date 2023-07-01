package com.example.authors.client.service


interface ArticleApiService {
    fun deleteArticlesByAuthorId(authorId: Int)
}