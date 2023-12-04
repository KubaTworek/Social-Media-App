package pl.jakubtworek.articles.service

import pl.jakubtworek.articles.controller.dto.LikeInfoResponse
import pl.jakubtworek.articles.controller.dto.LikeResponse

interface LikeService {
    fun like(articleId: Int, jwt: String) : LikeResponse
    fun getLikeInfo(articleId: Int) : LikeInfoResponse
}