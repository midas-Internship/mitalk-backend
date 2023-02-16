package com.example.mitalk.domain.record.controller.data.response

import com.example.mitalk.domain.record.domain.entity.CounsellingType
import java.time.LocalDateTime
import java.util.UUID

data class GetRecordResponse(
    val records: List<RecordElement>
) {
    data class RecordElement(
        val recordId: UUID,
        val type: CounsellingType,
        val counsellorName: String,
        val customerName: String,
        val startAt: LocalDateTime
    )
}