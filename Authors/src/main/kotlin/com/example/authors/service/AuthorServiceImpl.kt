package com.example.authors.service

import com.example.authors.controller.AuthorRequest
import com.example.authors.model.Author
import com.example.authors.factories.AuthorFactory
import com.example.authors.repository.AuthorRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import java.util.*

@Service
@RequiredArgsConstructor
class AuthorServiceImpl(
    private val authorRepository: AuthorRepository,
    private val authorFactory: AuthorFactory
) : AuthorService {
    override fun findAllAuthors(): List<Author> {
        return authorRepository.findAll()
    }

    override fun findById(theId: Int): Optional<Author> {
        return authorRepository.findById(theId)
    }

    override fun findAllByKeyword(theKeyword: String): List<Author> {
        val authors: List<Author> = authorRepository.findAll()
        return authors.stream()
            .filter { it.firstName.contains(theKeyword) || it.lastName.contains(theKeyword) }
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