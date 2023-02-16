package com.example.mitalk.domain.record.domain.repository

import com.example.mitalk.domain.record.domain.entity.Record
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface RecordRepository: MongoRepository<Record, UUID> {
    fun findByCustomerId(customerId: UUID): List<Record>
    fun findByCounsellorId(counsellorId: UUID): List<Record>
}