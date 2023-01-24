package com.example.articles.service;

import com.example.articles.entity.Magazine;
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
public class MagazineServiceTest {
    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private MagazineService magazineService;

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
    public void findByIdMagazineTest(){
        Magazine magazine = magazineService.findById(1);

        assertEquals(1,magazine.getId());
        assertEquals("Times",magazine.getName());
    }

    @Test
    public void findByNameMagazineTest(){
        Magazine magazine = magazineService.findByName("Times");

        assertEquals(1,magazine.getId());
    }

    @Test
    public void saveMagazineTest(){
        Magazine magazine = new Magazine();
        magazine.setName("Forbes");
        magazineService.save(magazine);

        assertEquals("Times",magazineService.findById(1).getName());
        assertEquals("Forbes",magazineService.findById(2).getName());
    }

    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute("DELETE FROM Article");
        jdbc.execute("DELETE FROM Author");
        jdbc.execute("DELETE FROM Magazine");
        jdbc.execute("DELETE FROM Content");
    }
}
