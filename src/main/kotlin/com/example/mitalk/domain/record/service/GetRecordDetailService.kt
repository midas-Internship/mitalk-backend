package com.example.mitalk.domain.record.service

import com.example.mitalk.domain.record.presentation.data.response.GetRecordDetailResponse
import com.example.mitalk.domain.record.domain.repository.RecordRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class GetRecordDetailService(
    private val recordRepository: RecordRepository
) {
    @Transactional(readOnly = true)
    fun execute(recordId: UUID): GetRecordDetailResponse {
        val record = recordRepository.findByIdOrNull(recordId)?: TODO("Record Not Found Exception")

        val messageRecords = record.messageRecords.map {
            GetRecordDetailResponse.MessageRecordElement(
                id = it.messageId,
                sender = it.sender,
                isFile = it.isFile,
                isDeleted = it.isDeleted,
                isUpdated = it.isUpdated,
                dataMap = it.dataMap
            )
        }

        return GetRecordDetailResponse(
            startAt = record.startAt,
            customerName = record.customerName,
            counsellorName = record.counsellorName,
            messageRecords = messageRecords
        )
    }
}