package com.example.mitalk

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MitalkApplication

fun main(args: Array<String>) {
    runApplication<MitalkApplication>(*args)
}
