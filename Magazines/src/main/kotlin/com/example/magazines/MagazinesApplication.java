package com.example.magazines;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MagazinesApplication {
    public static void main(String[] args) {
        SpringApplication.run(MagazinesApplication.class, args);
    }
}