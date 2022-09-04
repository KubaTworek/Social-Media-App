package com.example.RESTAPIarticle.service;

import com.example.RESTAPIarticle.entity.Author;
public interface AuthorService {
    Author findById(int theId);
    void save(Author theAuthor);

    Author findByFirstNameAndLastName(String firstName, String lastName);
}
