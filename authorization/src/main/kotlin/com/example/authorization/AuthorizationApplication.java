package com.example.authorization;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.security.config.annotation.web.configuration.*;

@SpringBootApplication
@EnableWebSecurity
public class AuthorizationApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthorizationApplication.class, args);
    }
}
