package com.example.mitalk.domain.customer.domain.entity

import com.example.mitalk.domain.record.domain.entity.CounsellingType
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.redis.core.RedisHash
import java.util.UUID

@RedisHash
class CustomerInfo(
    @Id
    val customerId: UUID,

    @Indexed
    val customerSessionId: UUID,

    val type: CounsellingType
)