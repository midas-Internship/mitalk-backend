package com.example.mitalk.domain.counsellor.persistence

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CounsellorRepository : MongoRepository<Counsellor, UUID> {
    fun findByStatusOrderByTodayCounsellingCountAsc(status: CounsellorStatus): List<Counsellor>
    fun findByRoomId(roomId: UUID): Counsellor?
}