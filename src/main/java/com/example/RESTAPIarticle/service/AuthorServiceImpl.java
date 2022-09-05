package com.example.RESTAPIarticle.service;

import com.example.RESTAPIarticle.dao.AuthorDAO;
import com.example.RESTAPIarticle.entity.Author;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService{
    private final AuthorDAO authorDAO;
    @Override
    public Author findById(int theId) {
        return authorDAO.findById(theId).orElse(null);
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
