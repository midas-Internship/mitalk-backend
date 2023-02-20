package com.example.mitalk.domain.record.controller.data.response

import com.example.mitalk.domain.auth.domain.Role
import com.example.mitalk.domain.record.domain.entity.MessageRecord
import java.time.LocalDateTime

data class GetRecordDetailResponse(
    val startAt: LocalDateTime,
    val customerName: String,
    val counsellorName: String,
    val messageRecords: List<MessageRecordElement>
) {
    data class MessageRecordElement(
        val sender: Role,
        val isFile: Boolean,
        val isDeleted: Boolean,
        val isUpdated: Boolean,
        val dataMap: MutableList<MessageRecord.MessageData>
    )
}