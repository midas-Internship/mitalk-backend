package com.example.mitalk.domain.customer.domain.repository

import com.example.mitalk.domain.customer.domain.entity.CustomerIdHash
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface CustomerIdHashRepository : CrudRepository<CustomerIdHash, UUID> {
    fun deleteByCustomerSessionId(customerSessionId: String)
    fun findByCustomerSessionId(customerSessionId: String): CustomerIdHash?
}