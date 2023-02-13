package com.example.mitalk

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession

@EnableScheduling
@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
class MitalkApplication

fun main(args: Array<String>) {
    runApplication<MitalkApplication>(*args)
}
