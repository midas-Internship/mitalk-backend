package com.example.mitalk.domain.admin.presentation.data.response

import com.example.mitalk.domain.record.domain.entity.CounsellingType
import java.time.LocalDateTime
import java.util.UUID

data class GetRecordListRequest(
    val id: UUID,
    val startAt: LocalDateTime,
    val counsellorName: String,
    val customerName: String,
    val type: CounsellingType
)
