package pl.jakubtworek.articles.service

import org.springframework.stereotype.Component
import pl.jakubtworek.articles.controller.dto.ArticleDetailsResponse
import pl.jakubtworek.articles.controller.dto.ArticleResponse
import pl.jakubtworek.articles.controller.dto.AuthorResponse
import pl.jakubtworek.articles.controller.dto.LikeDetailsResponse
import pl.jakubtworek.articles.entity.Article
import pl.jakubtworek.articles.external.AuthorApiService
import pl.jakubtworek.articles.repository.ArticleRepository
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

@Component
class ArticleResponseFactory(
    private val authorService: AuthorApiService,
    private val articleRepository: ArticleRepository
) {
    fun createResponse(theArticle: Article, userId: Int): ArticleResponse {
        val author = authorService.getAuthorById(theArticle.authorId)
        val likerFullNames = theArticle.likes.map { like ->
            val liker = authorService.getAuthorById(like.authorId)
            "${liker.firstName} ${liker.lastName}"
        }
        val comments = articleRepository.countAllByMotherArticleId(theArticle.id)

        return ArticleResponse(
            id = theArticle.id,
            text = theArticle.content,
            timestamp = theArticle.createAt,
            elapsed = getTimeElapsed(theArticle.createAt),
            createDate = formatDateTime(theArticle.createAt),
            author = author.let {
                AuthorResponse(
                    id = it.id,
                    username = it.username,
                    firstName = it.firstName,
                    lastName = it.lastName,
                    isFollowed = it.followers.contains(userId)
                )
            },
            likes = LikeDetailsResponse(
                users = likerFullNames
            ),
            numOfComments = comments
        )
    }

    fun createResponseForOneArticle(theArticle: Article, userId: Int): ArticleDetailsResponse {
        val author = authorService.getAuthorById(theArticle.authorId)
        val likerFullNames = theArticle.likes.map { like ->
            val liker = authorService.getAuthorById(like.authorId)
            "${liker.firstName} ${liker.lastName}"
        }

        return ArticleDetailsResponse(
            id = theArticle.id,
            text = theArticle.content,
            timestamp = theArticle.createAt,
            elapsed = getTimeElapsed(theArticle.createAt),
            createDate = formatDateTime(theArticle.createAt),
            author = author.let {
                AuthorResponse(
                    id = it.id,
                    username = it.username,
                    firstName = it.firstName,
                    lastName = it.lastName,
                    isFollowed = it.followers.contains(userId)
                )
            },
            likes = LikeDetailsResponse(
                users = likerFullNames
            ),
            comments = mutableListOf()
        )
    }

    private fun getTimeElapsed(timestamp: Timestamp): String {
        val now = Timestamp.from(Instant.now())
        val timeDiff = now.time - timestamp.time

        return when {
            timeDiff < 60000 -> "${timeDiff / 1000}s"
            timeDiff < 3600000 -> "${timeDiff / 60000}m"
            timeDiff < 86400000 -> "${timeDiff / 3600000}h"
            timeDiff < 604800000 -> "${timeDiff / 86400000}d"
            else -> "${timeDiff / 604800000}w"
        }
    }

    private fun formatDateTime(inputDate: Timestamp): String {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = inputDate.time
        }

        val hour = calendar[Calendar.HOUR_OF_DAY]
        val minute = calendar[Calendar.MINUTE]
        val day = calendar[Calendar.DAY_OF_MONTH]
        val month = SimpleDateFormat("MMM", Locale.US).format(inputDate)
        val year = calendar[Calendar.YEAR]

        val formattedTime = formatTime(hour, minute)
        val formattedDate = "$month $day, $year"

        return "$formattedTime · $formattedDate"
    }

    private fun formatTime(hour: Int, minute: Int): String {
        val isPM = hour >= 12
        val formattedHour = hour % 12
        val period = if (isPM) "PM" else "AM"

        return "${if (formattedHour == 0) 12 else formattedHour}:$minute $period"
    }
}