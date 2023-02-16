package com.example.mitalk.domain.record.service

import com.example.mitalk.domain.counsellor.domain.repository.CounsellorRepository
import com.example.mitalk.domain.customer.domain.repository.CustomerRepository
import com.example.mitalk.domain.record.controller.data.response.GetRecordResponse
import com.example.mitalk.domain.record.domain.entity.Record
import com.example.mitalk.domain.record.domain.repository.RecordRepository
import com.example.mitalk.global.util.UserUtil
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class GetRecordService(
    private val counsellorRepository: CounsellorRepository,
    private val customerRepository: CustomerRepository
) {

    fun execute(record: List<Record>): GetRecordResponse {

        val recordElements = record.map {
            GetRecordResponse.RecordElement(
                it.id,
                it.counsellingType,
                (counsellorRepository.findByIdOrNull(it.counsellorId)?: TODO("counsellor Not Found")).name,
                (customerRepository.findByIdOrNull(it.customerId)?: TODO("customer Nof Found")).name,
                it.startAt
            )
        }

        return GetRecordResponse(recordElements)
    }
}