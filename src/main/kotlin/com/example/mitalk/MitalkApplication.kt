package com.example.mitalk

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableScheduling

@ConfigurationPropertiesScan
@EnableScheduling
@ComponentScan("com.example.mitalk.domain.customer.domain.repository")tatus
@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
class MitalkApplication

fun main(args: Array<String>) {
    runApplication<MitalkApplication>(*args)
}
