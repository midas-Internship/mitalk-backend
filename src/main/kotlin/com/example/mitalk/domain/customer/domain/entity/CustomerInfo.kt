package com.example.mitalk.domain.customer.domain.entity

import com.example.mitalk.domain.record.domain.entity.CounsellingType
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import java.util.UUID

@RedisHash
class CustomerInfo(
    @Id
    val customerId: UUID,

    @Indexed
    val customerSessionId: String,

    val type: CounsellingType
)