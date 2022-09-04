package com.example.RESTAPIarticle.dao;

import com.example.RESTAPIarticle.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorDAO extends JpaRepository<Author, Integer> {
    Author findByFirstNameAndLastName(String firstName, String lastName);
}
