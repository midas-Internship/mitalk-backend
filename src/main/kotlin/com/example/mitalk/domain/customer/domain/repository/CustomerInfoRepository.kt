package com.example.mitalk.domain.customer.domain.repository

import com.example.mitalk.domain.customer.domain.entity.CustomerInfo
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface CustomerInfoRepository : CrudRepository<CustomerInfo, UUID> {
    fun deleteByCustomerSessionId(customerSessionId: String)
    fun findByCustomerSessionId(customerSessionId: String): CustomerInfo?
}