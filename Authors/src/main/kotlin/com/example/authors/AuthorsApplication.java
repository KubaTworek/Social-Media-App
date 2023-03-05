package com.example.authors;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.cloud.openfeign.*;

@EnableFeignClients
@SpringBootApplication
public class AuthorsApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthorsApplication.class, args);
    }
}