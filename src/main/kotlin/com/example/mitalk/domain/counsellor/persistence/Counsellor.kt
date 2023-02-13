package com.example.mitalk.domain.counsellor.persistence

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.web.socket.WebSocketSession
import java.util.UUID

@Document("counsellor")
data class Counsellor(
    @Id
    val id: UUID,

    val temporaryId: UUID?,

    val counsellorSession: WebSocketSession?,

    val customerSession: WebSocketSession?,

    val todayCounsellingCount: Int = 0,

    val status: CounsellorStatus = CounsellorStatus.OFFLINE
) {
    fun sessionConnectEvent(counsellorSession: WebSocketSession) = Counsellor(
            id, temporaryId, counsellorSession, customerSession, todayCounsellingCount, CounsellorStatus.ONLINE
    )

    fun counsellingEvent(temporaryId: UUID, customerSession: WebSocketSession) = Counsellor(
        id, temporaryId, counsellorSession, customerSession, todayCounsellingCount.plus(1), CounsellorStatus.COUNSELLING
    )
}

enum class CounsellorStatus{
    OFFLINE, ONLINE, COUNSELLING
}
