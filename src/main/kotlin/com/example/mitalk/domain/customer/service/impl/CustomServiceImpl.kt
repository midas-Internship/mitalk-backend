package com.example.mitalk.domain.customer.service.impl

import com.example.mitalk.domain.customer.service.CustomService
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import org.springframework.web.socket.WebSocketSession

@Service
class CustomServiceImpl(
        val redisTemplate: StringRedisTemplate
) : CustomService {
//    fun(ws: WebSocketSession) {
//        val a = findLastCountValueFromRedis()
//        if (a > 30) {
//            val now: Long = System.currentTimeMillis()
//            redisTemplate.opsForZSet().add("a", "amlkdam", now.toDouble())
//            redisTemplate.opsForZSet().range("a", 0, -1)
//            redisTemplate.opsForZSet().rank("a", "a")
//        } else {
//
//        }
//    }

//    fun findLastCountValueFromRedis() = redisTemplate.opsForZSet().rank("a", "anlkadmla")
}