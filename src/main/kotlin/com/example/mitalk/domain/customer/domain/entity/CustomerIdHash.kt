package com.example.mitalk.domain.customer.domain.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.redis.core.RedisHash
import java.util.UUID

@RedisHash
class CustomerIdHash(
    @Id
    val customerId: UUID,

    @Indexed
    val customerSessionId: String
)