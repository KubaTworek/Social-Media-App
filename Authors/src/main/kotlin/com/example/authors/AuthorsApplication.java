package com.example.authors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AuthorsApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthorsApplication.class, args);
    }
}
