package com.example.mitalk.domain.customer.service.impl

import com.example.mitalk.domain.customer.domain.repository.CustomerRepository
import com.example.mitalk.domain.customer.exception.CustomerNotFoundException
import com.example.mitalk.domain.customer.presentation.data.request.SignInRequest
import com.example.mitalk.domain.customer.service.SignInService

class SignInServiceImpl(
        private val customerRepository: CustomerRepository
) : SignInService {
    override fun execute(requestDto: SignInRequest) {
        val customer = customerRepository.findByEmail(requestDto.email)
                ?: throw CustomerNotFoundException()
        if(customer != null) {

        }
    }
}