package com.example.mitalk.domain.customer.util

import com.example.mitalk.domain.customer.domain.entity.Customer
import com.example.mitalk.domain.customer.domain.repository.CustomerRepository
import com.example.mitalk.domain.customer.presentation.data.request.SignInRequest
import org.springframework.stereotype.Component

@Component
class CustomerUtil(
        private val customerRepository: CustomerRepository
) {
    fun saveNewCustomer(request: SignInRequest, refreshToken: String) {
        val customer = Customer(
                id = 0,
                name = request.name,
                email = request.email,
                picture = request.profileImg
        )
        customerRepository.save(customer)
    }

    fun saveNewRefreshToken(customer: Customer, refreshToken: String) {
        return TODO()
    }
}