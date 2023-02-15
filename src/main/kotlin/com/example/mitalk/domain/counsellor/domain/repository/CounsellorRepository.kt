package com.example.mitalk.domain.counsellor.domain.repository

import com.example.mitalk.domain.counsellor.domain.entity.Counsellor
import com.example.mitalk.domain.counsellor.domain.entity.CounsellorStatus
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CounsellorRepository : MongoRepository<Counsellor, UUID> {
    fun findByStatusOrderByTodayCounsellingCountAsc(status: CounsellorStatus): List<Counsellor>
    fun findByRoomId(roomId: UUID): Counsellor?
    fun findByCounsellorSession(counsellor: String): Counsellor?
    fun findByCustomerSession(customer: String): Counsellor?

}