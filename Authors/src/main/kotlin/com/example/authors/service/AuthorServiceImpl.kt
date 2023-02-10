package com.example.authors.service

import com.example.authors.controller.AuthorRequest
import com.example.authors.controller.AuthorResponse
import com.example.authors.factories.AuthorFactory
import com.example.authors.model.Author
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
    override fun findAllAuthors(): List<AuthorResponse> {
        val authors = authorRepository.findAll()
        val authorsResponseList = mutableListOf<AuthorResponse>()
        for(author in authors){
            val response = authorFactory.createResponse(author)
            authorsResponseList.add(response)
        }
        return authorsResponseList
    }

    override fun findById(theId: Int): Optional<Author> {
        return authorRepository.findById(theId)
    }

    override fun findAllByKeyword(theKeyword: String): List<AuthorResponse> {
        val authors: List<Author> = authorRepository.findAll()
            .stream()
            .filter { it.firstName.contains(theKeyword) || it.lastName.contains(theKeyword) }
            .toList()
        val authorsResponseList = mutableListOf<AuthorResponse>()
        for(author in authors){
            val response = authorFactory.createResponse(author)
            authorsResponseList.add(response)
        }
        return authorsResponseList
    }

    override fun save(theAuthor: AuthorRequest) {
        val author = authorFactory.createAuthor(theAuthor)

        authorRepository.save(author)
    }

    override fun deleteById(theId: Int) {
        authorRepository.deleteById(theId)
    }

}