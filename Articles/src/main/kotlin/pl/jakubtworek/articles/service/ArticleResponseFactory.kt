package pl.jakubtworek.articles.service

import org.springframework.stereotype.Component
import pl.jakubtworek.articles.controller.dto.ArticleOneResponse
import pl.jakubtworek.articles.controller.dto.ArticleResponse
import pl.jakubtworek.articles.controller.dto.AuthorResponse
import pl.jakubtworek.articles.controller.dto.LikeInfoResponse
import pl.jakubtworek.articles.entity.Article
import pl.jakubtworek.articles.external.AuthorApiService

@Component
class ArticleResponseFactory(
    private val authorService: AuthorApiService
) {
    fun createResponse(theArticle: Article, userId: Int): ArticleResponse {
        val author = authorService.getAuthorById(theArticle.authorId)
        val likerFullNames = theArticle.likes.map { like ->
            val liker = authorService.getAuthorById(like.authorId)
            "${liker.firstName} ${liker.lastName}"
        }

        return ArticleResponse(
            id = theArticle.id,
            text = theArticle.content,
            timestamp = theArticle.createAt,
            author = author.let {
                AuthorResponse(
                    id = it.id,
                    username = it.username,
                    firstName = it.firstName,
                    lastName = it.lastName,
                    isFollowed = it.followers.contains(userId)
                )
            },
            likes = LikeInfoResponse(
                users = likerFullNames
            )
        )
    }

    fun createResponseForOneArticle(theArticle: Article, userId: Int): ArticleOneResponse {
        val author = authorService.getAuthorById(theArticle.authorId)
        val likerFullNames = theArticle.likes.map { like ->
            val liker = authorService.getAuthorById(like.authorId)
            "${liker.firstName} ${liker.lastName}"
        }

        return ArticleOneResponse(
            id = theArticle.id,
            text = theArticle.content,
            timestamp = theArticle.createAt,
            author = author.let {
                AuthorResponse(
                    id = it.id,
                    username = it.username,
                    firstName = it.firstName,
                    lastName = it.lastName,
                    isFollowed = it.followers.contains(userId)
                )
            },
            likes = LikeInfoResponse(
                users = likerFullNames
            ),
            comments = mutableListOf()
        )
    }
}