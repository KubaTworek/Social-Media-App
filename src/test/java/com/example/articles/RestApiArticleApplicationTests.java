package com.example.articles;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource("/application.properties")
@SpringBootTest
class RestApiArticleApplicationTests {

	@Test
	void contextLoads() {
	}

}
