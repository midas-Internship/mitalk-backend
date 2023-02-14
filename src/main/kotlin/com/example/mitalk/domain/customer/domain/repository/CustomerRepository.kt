package com.example.mitalk.domain.customer.domain.repository

import com.example.mitalk.domain.customer.domain.entity.Customer
import org.springframework.data.repository.CrudRepository
import java.util.UUID
import javax.validation.constraints.Email

interface CustomerRepository : CrudRepository<Customer, Long> {
    fun findByEmail(email: String): Customer?
}