package com.example.mitalk

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class MitalkApplication

fun main(args: Array<String>) {
    runApplication<MitalkApplication>(*args)
}
