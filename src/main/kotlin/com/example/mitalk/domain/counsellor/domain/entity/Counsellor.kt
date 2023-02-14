package com.example.mitalk.domain.counsellor.domain.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

@Document("counsellor")
data class Counsellor(
    @Id
    val id: UUID,

    @Indexed(unique = true)
    val roomId: UUID?,

    val counsellorSession: String?,

    val customerSession: String?,

    val todayCounsellingCount: Int = 0,

    val status: CounsellorStatus = CounsellorStatus.OFFLINE
) {
    fun sessionConnectEvent(counsellorSession: String) = Counsellor(
            id, roomId, counsellorSession, customerSession, todayCounsellingCount, CounsellorStatus.ONLINE
    )

    fun counsellingEvent(temporaryId: UUID, customerSession: String) = Counsellor(
        id, temporaryId, counsellorSession, customerSession, todayCounsellingCount.plus(1), CounsellorStatus.COUNSELLING
    )
}

enum class CounsellorStatus{
    OFFLINE, ONLINE, COUNSELLING
}
