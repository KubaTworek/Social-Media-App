package com.example.RESTAPIarticle.service

import com.example.RESTAPIarticle.entity.Author

interface AuthorService {
    fun findById(theId: Int): Author?
    fun save(theAuthor: Author)
    fun findByFirstNameAndLastName(firstName: String, lastName: String): Author?
}