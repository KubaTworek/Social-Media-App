package com.example.RESTAPIarticle.service

import com.example.RESTAPIarticle.entity.Author
import com.example.RESTAPIarticle.repository.AuthorRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service

@Service
@RequiredArgsConstructor
class AuthorServiceImpl(private val authorRepository: AuthorRepository) : AuthorService {

    override fun findById(theId: Int): Author? {
        return authorRepository.findById(theId).orElse(null)
    }

    override fun save(theAuthor: Author) {
        theAuthor.id = 0
        authorRepository.save(theAuthor)
    }

    override fun findByFirstNameAndLastName(firstName: String, lastName: String): Author? {
        return authorRepository.findByFirstNameAndLastName(firstName, lastName)
    }
}