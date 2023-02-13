package com.example.mitalk.domain.customer.service.impl

import com.example.mitalk.domain.customer.service.QueueCheckService
import com.example.mitalk.global.redis.util.RedisUtils
import com.example.mitalk.global.socket.dto.Testdata
import org.springframework.stereotype.Service

@Service
class QueueCheckServiceImpl(
        private val redisUtils: RedisUtils
) : QueueCheckService {
    companion object {
        var i: Long = 1
    }
    override fun execute() {
        val data = Testdata("$i")
        redisUtils.opsForZSet()
        redisUtils.createObject("test", "$data")
        i += 1
    }
}