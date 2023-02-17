package com.example.mitalk.domain.admin.service

import com.example.mitalk.domain.admin.presentation.data.response.GetRecordListRequest
import com.example.mitalk.domain.counsellor.domain.repository.CounsellorRepository
import com.example.mitalk.domain.customer.domain.repository.CustomerRepository
import com.example.mitalk.domain.record.domain.repository.RecordRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class GetRecordListService(
    private val recordRepository: RecordRepository,
    private val customerRepository: CustomerRepository,
    private val counsellorRepository: CounsellorRepository
) {

    fun execute(): List<GetRecordListRequest> {
        return recordRepository.findAll().map {
            GetRecordListRequest(
                id = it.id,
                startAt = it.startAt,
                counsellorName = (counsellorRepository.findByIdOrNull(it.counsellorId) ?: TODO("Counsellor notfound")).name,
                customerName = (customerRepository.findByIdOrNull(it.customerId) ?: TODO("Customer notfound")).name,
                type = it.counsellingType
            )
        }
    }
}