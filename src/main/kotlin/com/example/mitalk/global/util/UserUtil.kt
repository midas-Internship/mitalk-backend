package com.example.mitalk.global.util

import com.example.mitalk.domain.customer.domain.entity.Customer
import com.example.mitalk.domain.customer.domain.repository.CustomerRepository
import com.example.mitalk.global.security.auth.CustomerDetails
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class UserUtil(
        private val customerRepository: CustomerRepository
) {
    fun getCurrentCustomer(): Customer {
        val principal = SecurityContextHolder.getContext().authentication.principal as CustomerDetails
        val email = principal.username
        return customerRepository.findByEmail(email) ?: TODO("UserNotfound:")
    }
}