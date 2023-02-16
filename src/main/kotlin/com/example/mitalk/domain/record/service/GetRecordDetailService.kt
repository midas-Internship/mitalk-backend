package com.example.mitalk.domain.record.service

import com.example.mitalk.domain.counsellor.domain.repository.CounsellorRepository
import com.example.mitalk.domain.customer.domain.repository.CustomerRepository
import com.example.mitalk.domain.record.controller.data.response.GetRecordDetailResponse
import com.example.mitalk.domain.record.domain.repository.RecordRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetRecordDetailService(
    private val recordRepository: RecordRepository,
    private val counsellorRepository: CounsellorRepository,
    private val customerRepository: CustomerRepository
) {

    fun execute(recordId: UUID): GetRecordDetailResponse {
        val record = recordRepository.findByIdOrNull(recordId)?: TODO("Record Not Found Exception")

        val messageRecords = record.messageRecords.map {
            GetRecordDetailResponse.MessageRecordElement(
                sender = it.sender,
                isFile = it.isFile,
                isDeleted = it.isDeleted,
                isUpdated = it.isUpdated,
                dataMap = it.dataMap
            )
        }

        return GetRecordDetailResponse(
            startAt = record.startAt,
            customerName = (counsellorRepository.findByIdOrNull(record.counsellorId)?: TODO("counsellor Not Found")).name,
            counsellorName = (customerRepository.findByIdOrNull(record.customerId)?: TODO("customer Nof Found")).name,
            messageRecords = messageRecords
        )
    }
}