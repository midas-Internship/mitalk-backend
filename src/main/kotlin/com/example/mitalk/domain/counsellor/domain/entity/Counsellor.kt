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

    val name: String,

    val counsellorSession: String?,

    val customerSession: String?,

    val todayCounsellingCount: Int = 0,

    val status: CounsellorStatus = CounsellorStatus.OFFLINE
) {
    constructor(name: String): this(UUID.randomUUID(), null, name, null, null)

    fun reconnect(customerSession: String) = Counsellor(
        id, roomId, name, counsellorSession, customerSession, todayCounsellingCount, CounsellorStatus.COUNSELLING
    )
    fun sessionConnectEvent(counsellorSession: String) = Counsellor(
        id, roomId, name, counsellorSession, customerSession, todayCounsellingCount, CounsellorStatus.ONLINE
    )

    fun counsellingEvent(temporaryId: UUID, customerSession: String) = Counsellor(
        id, temporaryId, name, counsellorSession, customerSession, todayCounsellingCount.plus(1), CounsellorStatus.COUNSELLING
    )

    fun roomCloseEvent() = Counsellor(
        id, roomId = null, name, counsellorSession = null, customerSession = null, todayCounsellingCount, CounsellorStatus.OFFLINE
    )

}
enum class CounsellorStatus{
    OFFLINE, ONLINE, COUNSELLING
}
