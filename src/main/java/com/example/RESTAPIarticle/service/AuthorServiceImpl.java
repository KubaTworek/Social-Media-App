package com.example.RESTAPIarticle.service;

import com.example.RESTAPIarticle.dao.AuthorDAO;
import com.example.RESTAPIarticle.entity.Author;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService{
    private final AuthorDAO authorDAO;

    public AuthorServiceImpl(AuthorDAO authorDAO) {
        this.authorDAO = authorDAO;
    }

    @Override
    public Author findById(int theId) {
        Optional<Author> result = authorDAO.findById(theId);

        Author theAuthor = null;

        if (result.isPresent()) {
            theAuthor = result.get();
        }

        return theAuthor;
    }

    @Override
    public void save(Author theAuthor) {
        theAuthor.setId(0);
        authorDAO.save(theAuthor);
    }

    @Override
    public Author findByFirstNameAndLastName(String firstName, String lastName) {
        return authorDAO.findByFirstNameAndLastName(firstName,lastName);
    }
}
