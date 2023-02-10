package com.example.mitalk.domain.customer.domain.entity

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash

@RedisHash("customerSession")
data class CustomerSession(
        @Id
        val session: String,

)