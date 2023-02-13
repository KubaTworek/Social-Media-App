package com.example.magazines.model.dto

data class ArticleContentDTO(
    val id: Int,
    val title: String,
    val text: String
) {
    constructor() : this(0, "", "")
}