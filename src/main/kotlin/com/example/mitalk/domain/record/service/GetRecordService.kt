package com.example.mitalk.domain.record.service

import com.example.mitalk.domain.record.presentation.data.response.GetRecordResponse
import com.example.mitalk.domain.record.domain.entity.Record
import org.springframework.stereotype.Service

@Service
class GetRecordService(
) {

    fun execute(record: List<Record>): GetRecordResponse {

        val recordElements = record.map {
            GetRecordResponse.RecordElement(
                it.id,
                it.counsellingType,
                it.counsellorName,
                it.customerName,
                it.startAt
            )
        }

        return GetRecordResponse(recordElements)
    }
}