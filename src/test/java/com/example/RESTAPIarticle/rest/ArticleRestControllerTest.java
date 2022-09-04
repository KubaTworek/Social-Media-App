package com.example.RESTAPIarticle.rest;

import com.example.RESTAPIarticle.entity.Article;
import com.example.RESTAPIarticle.entity.ArticleContent;
import com.example.RESTAPIarticle.entity.Author;
import com.example.RESTAPIarticle.entity.Magazine;
import com.example.RESTAPIarticle.errors.ArticleNotFoundException;
import com.example.RESTAPIarticle.service.ArticleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application.properties")
@AutoConfigureMockMvc
@SpringBootTest(properties = "spring.main.lazy-initialization=true")
@Transactional
public class ArticleRestControllerTest {
    private static MockHttpServletRequest request;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private Article article;

    @Autowired
    private ArticleContent content;

    @Autowired
    private Author author;

    @Autowired
    private Magazine magazine;

    public static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    @BeforeEach
    public void setupDatabase() {
        jdbc.execute("DELETE FROM Author");
        jdbc.execute("DELETE FROM Magazine");
        jdbc.execute("DELETE FROM Content");
        jdbc.execute("DELETE FROM Article");
        jdbc.execute("INSERT INTO Author(id, first_name, last_name)" + "VALUES (1,'Tim','Cook')");
        jdbc.execute("INSERT INTO Magazine(id, name)" + "VALUES (1,'Times')");
        jdbc.execute("INSERT INTO Content(id, title, text)" + "VALUES (1,'Title','Text')");
        jdbc.execute("INSERT INTO Article(id, content_id, date, magazine_id, author_id, timestamp)" + "VALUES (1,1,'2022-10-22', 1, 1, 0)");
    }

    @Test
    public void getArticlesHttpRequest() throws Exception {
        author.setId(0);
        author.setFirstName("John");
        author.setLastName("Smith");
        entityManager.persist(author);
        entityManager.flush();
        magazine.setId(0);
        magazine.setName("Forbes");
        entityManager.persist(magazine);
        entityManager.flush();
        content.setId(0);
        content.setText("Text");
        content.setTitle("Title");
        article.setId(0);
        article.setAuthor(author);
        article.setMagazine(magazine);
        article.setContent(content);
        article.setDate("2022-10-23");
        articleService.save(article);


        mockMvc.perform(MockMvcRequestBuilders.get("/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getArticleByIdHttpRequest() throws Exception {
        Article article = articleService.findById(1);

        assertNotNull(article);
        assertThrows(ArticleNotFoundException.class,() -> articleService.findById(2));

        mockMvc.perform(MockMvcRequestBuilders.get("/article/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.date", is("2022-10-22")));
    }

    @Test
    public void getArticlesByKeywordHttpRequest() throws Exception {
        author.setId(0);
        author.setFirstName("John");
        author.setLastName("Smith");
        entityManager.persist(author);
        entityManager.flush();
        magazine.setId(0);
        magazine.setName("Forbes");
        entityManager.persist(magazine);
        entityManager.flush();
        content.setId(0);
        content.setText("Description");
        content.setTitle("Title");
        article.setId(0);
        article.setAuthor(author);
        article.setMagazine(magazine);
        article.setContent(content);
        article.setDate("2022-10-23");
        articleService.save(article);


        mockMvc.perform(MockMvcRequestBuilders.get("/articles/title"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)));

        mockMvc.perform(MockMvcRequestBuilders.get("/articles/text"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(MockMvcRequestBuilders.get("/articles/random"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(0)));
    }


/*
    @Test
    public void saveArticleHttpRequest() throws Exception {
        author.setId(0);
        author.setFirstName("John");
        author.setLastName("Smith");
        entityManager.persist(author);
        entityManager.flush();
        magazine.setId(0);
        magazine.setName("Forbes");
        entityManager.persist(magazine);
        entityManager.flush();
        content.setId(0);
        content.setText("Description");
        content.setTitle("Title");
        article.setId(0);
        article.setAuthor(author);
        article.setMagazine(magazine);
        article.setContent(content);
        article.setDate("2022-10-23");

        mockMvc.perform(post("/article")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article)))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)));
    }*/

    @Test
    @Order(1)
    public void deleteArticleHttpRequest() throws Exception {
        assertNotNull(articleService.findById(1));

        mockMvc.perform(MockMvcRequestBuilders.delete("/article/{id}", 1))
                .andExpect(status().isOk());

        assertThrows(ArticleNotFoundException.class,() -> articleService.findById(2));

        mockMvc.perform(MockMvcRequestBuilders.get("/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute("DELETE FROM Author");
        jdbc.execute("DELETE FROM Magazine");
        jdbc.execute("DELETE FROM Content");
        jdbc.execute("DELETE FROM Article");
    }
}
