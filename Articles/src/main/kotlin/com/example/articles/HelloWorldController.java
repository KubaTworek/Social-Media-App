package com.example.articles;

import org.springframework.web.bind.annotation.*;

@RestController
public class HelloWorldController {
    @GetMapping("/helloworld")
    String helloWorld() {
        return "Hello World!";
    }
}
