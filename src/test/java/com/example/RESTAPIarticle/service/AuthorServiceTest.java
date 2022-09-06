package com.example.RESTAPIarticle.service;

import com.example.RESTAPIarticle.entity.Author;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestPropertySource("/application.properties")
@SpringBootTest
@Transactional
public class AuthorServiceTest {
    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private AuthorService authorService;

    @BeforeEach
    public void setupDatabase() {
        jdbc.execute("DELETE FROM Article");
        jdbc.execute("DELETE FROM Author");
        jdbc.execute("DELETE FROM Magazine");
        jdbc.execute("DELETE FROM Content");
        jdbc.execute("INSERT INTO Author(id, first_name, last_name)" + "VALUES (1,'Tim','Cook')");
        jdbc.execute("INSERT INTO Magazine(id, name)" + "VALUES (1,'Times')");
        jdbc.execute("INSERT INTO Content(id, title, text)" + "VALUES (1,'Title','Text')");
        jdbc.execute("INSERT INTO Article(id, content_id, date, magazine_id, author_id, timestamp)" + "VALUES (1,1,'2022-10-22', 1, 1, 0)");
    }

    @Test
    public void findByIdAuthorTest(){
        Author author = authorService.findById(1);

        assertEquals(1,author.getId());
        assertEquals("Tim",author.getFirstName());
        assertEquals("Cook",author.getLastName());
    }

    @Test
    public void findByFirstNameAndLastNameAuthorTest(){
        Author author = authorService.findByFirstNameAndLastName("Tim","Cook");

        assertEquals(1,author.getId());
    }

    @Test
    public void saveAuthorTest(){
        Author author = new Author();
        author.setId(2);
        author.setFirstName("John");
        author.setLastName("Smith");
        authorService.save(author);

        assertEquals("Tim",authorService.findById(1).getFirstName());
        assertEquals("Cook",authorService.findById(1).getLastName());
        assertEquals("John",authorService.findById(2).getFirstName());
        assertEquals("Smith",authorService.findById(2).getLastName());


    }

    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute("DELETE FROM Article");
        jdbc.execute("DELETE FROM Author");
        jdbc.execute("DELETE FROM Magazine");
        jdbc.execute("DELETE FROM Content");
    }
}
