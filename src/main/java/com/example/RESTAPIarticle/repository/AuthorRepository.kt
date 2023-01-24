package com.example.RESTAPIarticle.repository

import com.example.RESTAPIarticle.entity.Author
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AuthorRepository : JpaRepository<Author?, Int?> {
    fun findByFirstNameAndLastName(firstName: String?, lastName: String?): Author?
}