package com.example.RESTAPIarticle.rest;

import com.example.RESTAPIarticle.entity.Article;
import com.example.RESTAPIarticle.entity.ArticleContent;
import com.example.RESTAPIarticle.entity.Author;
import com.example.RESTAPIarticle.entity.Magazine;
import com.example.RESTAPIarticle.service.ArticleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application.properties")
@AutoConfigureMockMvc
@SpringBootTest
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
        author.setId(1);
        author.setFirstName("John");
        author.setLastName("Smith");
        magazine.setId(1);
        magazine.setName("Name");

    }

    @Test
    public void getEmployeesHttpRequest() throws Exception {
        content.setId(0);
        content.setTitle("Title");
        content.setText("Text");
        article.setId(1);
        article.setMagazine(magazine);
        article.setAuthor(author);
        article.setContent(content);
        article.setDate("2022-10-22");
        entityManager.persist(article);
        entityManager.flush();


        mockMvc.perform(MockMvcRequestBuilders.get("/article/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    /*@Test
    public void getEmployeeByIdHttpRequest() throws Exception {
        Employee employee = employeeService.findById(1);

        assertNotNull(employee);

        mockMvc.perform(MockMvcRequestBuilders.get("/employee/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Smith")));
    }

    @Test
    public void saveEmployeeHttpRequest() throws Exception {
        employee.setFirstName("Adam");
        employee.setLastName("Johnson");

        mockMvc.perform(post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    public void deleteEmployeeHttpRequest() throws Exception {
        assertNotNull(employeeService.findById(1));

        mockMvc.perform(MockMvcRequestBuilders.delete("/employee/{id}", 1))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void getEmployeeByJobHttpRequest() throws Exception {
        List<Employee> employees = employeeService.findByJob("Cook");

        assertNotNull(employees);

        mockMvc.perform(MockMvcRequestBuilders.get("/employees/{jobName}", "Cook"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)));
    }
*/



    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute("DELETE FROM Article");
    }
}
