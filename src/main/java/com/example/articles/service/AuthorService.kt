package com.example.articles.service

import com.example.articles.entity.Author

interface AuthorService {
    fun findById(theId: Int): Author?
    fun save(theAuthor: Author)
    fun findByFirstNameAndLastName(firstName: String, lastName: String): Author?
}