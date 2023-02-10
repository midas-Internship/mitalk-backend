package com.example.mitalk.domain.customer.service.impl

import com.example.mitalk.domain.customer.service.CustomService
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service

@Service
class CustomServiceImpl(
        val redisTemplate: StringRedisTemplate
) : CustomService {
    fun a() {
        val a = findLastCountValueFromRedis()
        if (a > 30) {
            val now: Long = System.currentTimeMillis()
            redisTemplate.opsForZSet().add("a", "amlkdam", now.toDouble())
        } else {

        }
    }

    fun findLastCountValueFromRedis() = redisTemplate.opsForZSet().count("a", 0.0, 100000000.0) ?: 0L
}