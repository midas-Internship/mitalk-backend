package com.example.mitalk.domain.customer.util

import com.example.mitalk.domain.auth.domain.entity.RefreshToken
import com.example.mitalk.domain.auth.domain.repository.RefreshTokenRepository
import com.example.mitalk.domain.customer.domain.entity.Customer
import com.example.mitalk.domain.customer.domain.repository.CustomerRepository
import com.example.mitalk.domain.customer.presentation.data.request.SignInRequest
import org.springframework.stereotype.Component
import java.util.*

@Component
class CustomerGeneratedUtil(
        private val customerRepository: CustomerRepository,
        private val refreshTokenRepository: RefreshTokenRepository
) {
    fun saveNewCustomer(request: SignInRequest, refreshToken: String) {
        val customer = Customer(
                id = UUID.randomUUID(),
                name = request.name,
                email = request.email,
                picture = request.profileImg
        )

        customerRepository.save(customer)

        val refreshToken = RefreshToken(
                userId = customer.id,
                token = refreshToken
        )
        refreshTokenRepository.save(refreshToken)
    }

    fun saveNewRefreshToken(customer: Customer, refreshToken: String) {
        val refreshToken = RefreshToken(
                userId = customer.id,
                token = refreshToken
        )
        refreshTokenRepository.save(refreshToken)
    }
}