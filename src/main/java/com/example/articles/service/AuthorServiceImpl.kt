package com.example.articles.service

import com.example.articles.controller.author.AuthorRequest
import com.example.articles.entity.AuthorPost
import com.example.articles.factories.AuthorFactory
import com.example.articles.repository.AuthorRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import java.util.*
import kotlin.streams.toList

@Service
@RequiredArgsConstructor
class AuthorServiceImpl(
    private val authorRepository: AuthorRepository,
    private val authorFactory: AuthorFactory
) : AuthorService {
    override fun findAllAuthors(): List<AuthorPost> {
        return authorRepository.findAll()
    }

    override fun findById(theId: Int): Optional<AuthorPost> {
        return authorRepository.findById(theId)
    }

    override fun findAllByKeyword(theKeyword: String): List<AuthorPost> {
        val authors: List<AuthorPost> = authorRepository.findAll()
        return authors.stream()
            .filter { authors: AuthorPost ->
                authors.firstName.contains(theKeyword) || authors.lastName.contains(theKeyword)
            }
            .toList()
    }

    override fun save(theAuthor: AuthorRequest) {
        val author = authorFactory.createAuthor(theAuthor)

        authorRepository.save(author)
    }

    override fun deleteById(theId: Int) {
        authorRepository.deleteById(theId)
    }

}