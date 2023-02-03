package com.example.authors

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AuthorsApplication

fun main(args: Array<String>) {
	runApplication<AuthorsApplication>(*args)
}
