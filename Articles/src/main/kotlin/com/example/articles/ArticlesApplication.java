package com.example.articles;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.cloud.openfeign.*;

@EnableFeignClients
@SpringBootApplication
public class ArticlesApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArticlesApplication.class, args);
    }
}
