package com.example.mitalk.domain.admin.service

import com.example.mitalk.domain.admin.presentation.data.response.GetCustomerListResponse
import com.example.mitalk.domain.customer.domain.repository.CustomerInfoRepository
import com.example.mitalk.domain.customer.domain.repository.CustomerRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetCustomerListService(
    private val customerRepository: CustomerRepository,
    private val customerInfoRepository: CustomerInfoRepository
) {
    @Transactional(readOnly = true)
    fun execute(): List<GetCustomerListResponse> {
        return customerRepository.findAll().map {
            GetCustomerListResponse(
                id = it.id,
                name = it.name,
                email = it.email,
                session = customerInfoRepository.findByIdOrNull(it.id)?.customerSessionId
            )
        }
    }
}