package com.example.magazines

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MagazinesApplication

fun main(args: Array<String>) {
	runApplication<MagazinesApplication>(*args)
}
