package com.example.mitalk.global.redis.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer


@Configuration
@EnableRedisRepositories
class RedisConfig(
) {

    @field:Value("\${spring.redis.host}")
    lateinit var redisHost: String
    val redisPort: Int = 6379

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory = LettuceConnectionFactory(redisHost, redisPort)

    @Bean
    fun redisTemplate(): RedisTemplate<String, Any>? {
        val redisTemplate = RedisTemplate<String, Any>()
        redisTemplate.setConnectionFactory(redisConnectionFactory())
        redisTemplate.keySerializer = StringRedisSerializer() // Key: String
        redisTemplate.valueSerializer = Jackson2JsonRedisSerializer(String::class.java) // Value: 직렬화에 사용할 Object 사용하기
        return redisTemplate
    }

}