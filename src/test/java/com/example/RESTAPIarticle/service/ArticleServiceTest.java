package com.example.RESTAPIarticle.service;

import com.example.RESTAPIarticle.entity.Article;
import com.example.RESTAPIarticle.entity.ArticleContent;
import com.example.RESTAPIarticle.entity.Author;
import com.example.RESTAPIarticle.entity.Magazine;
import com.example.RESTAPIarticle.errors.ArticleNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestPropertySource("/application.properties")
@SpringBootTest
@Transactional
public class ArticleServiceTest {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private ArticleService articleService;

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
    public void findAllArticlesTest(){
        Iterable<Article> iterableArticles = articleService.findAllOrderByDateDesc();

        List<Article> articles = new ArrayList<>();

        for(Article article : iterableArticles) {
            articles.add(article);
        }

        assertEquals(1,articles.size());
    }

    @Test
    public void findByIdArticleTest(){
        Article article = articleService.findById(1);

        assertEquals(1,article.getId());
        assertEquals("2022-10-22",article.getDate());
        assertEquals("Title",article.getContent().getTitle());
        assertEquals("Text",article.getContent().getText());
        assertEquals("Tim",article.getAuthor().getFirstName());
        assertEquals("Cook",article.getAuthor().getLastName());
        assertEquals("Times",article.getMagazine().getName());
        assertThrows(ArticleNotFoundException.class, () -> articleService.findById(2));
    }

    @Test
    public void findAllArticlesByKeywordTest(){
        Iterable<Article> iterableArticlesWithText = articleService.findAllByKeyword("Text");
        Iterable<Article> iterableArticlesWithTitle = articleService.findAllByKeyword("Title");
        Iterable<Article> iterableArticlesWithRandom = articleService.findAllByKeyword("Random");

        List<Article> articlesWithText = new ArrayList<>();
        List<Article> articlesWithTitle = new ArrayList<>();
        List<Article> articlesWithRandom = new ArrayList<>();

        for(Article article : iterableArticlesWithText) {
            articlesWithText.add(article);
        }
        for(Article article : iterableArticlesWithTitle) {
            articlesWithTitle.add(article);
        }
        for(Article article : iterableArticlesWithRandom) {
            articlesWithRandom.add(article);
        }

        assertEquals(1,articlesWithText.size());
        assertEquals(1,articlesWithTitle.size());
        assertEquals(0,articlesWithRandom.size());
    }

    @Test
    public void saveArticleTest(){
        Article article = new Article();
        Author author = new Author();
        author.setId(0);
        author.setFirstName("John");
        author.setLastName("Smith");
        entityManager.persist(author);
        entityManager.flush();
        Magazine magazine = new Magazine();
        magazine.setId(0);
        magazine.setName("Forbes");
        entityManager.persist(magazine);
        entityManager.flush();
        ArticleContent content = new ArticleContent();
        content.setId(0);
        content.setText("Text");
        content.setTitle("Text");
        article.setId(0);
        article.setAuthor(author);
        article.setMagazine(magazine);
        article.setContent(content);
        article.setDate("2022-10-23");
        articleService.save(article);

        Iterable<Article> iterableArticles = articleService.findAllOrderByDateDesc();

        List<Article> articles = new ArrayList<>();

        for(Article tempArticle : iterableArticles) {
            articles.add(tempArticle);
        }

        assertEquals(2,articles.size());
    }

    @Test
    public void deleteByIdArticleTest(){
        articleService.deleteById(1);

        Iterable<Article> iterableArticles = articleService.findAllOrderByDateDesc();

        List<Article> articles = new ArrayList<>();

        for(Article article : iterableArticles) {
            articles.add(article);
        }

        assertEquals(0,articles.size());
        assertThrows(ArticleNotFoundException.class, () -> articleService.findById(1));
    }

    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute("DELETE FROM Article");
        jdbc.execute("DELETE FROM Author");
        jdbc.execute("DELETE FROM Magazine");
        jdbc.execute("DELETE FROM Content");
    }

}
