package com.example.mitalk.domain.customer.domain.repository

import com.example.mitalk.domain.customer.domain.entity.Customer
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface CustomerRepository : CrudRepository<Customer, UUID> {
    fun findByEmail(email: String): Customer?
}