package com.example.authors.repository

import com.example.authors.model.Author
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface AuthorRepository : JpaRepository<Author, Int>