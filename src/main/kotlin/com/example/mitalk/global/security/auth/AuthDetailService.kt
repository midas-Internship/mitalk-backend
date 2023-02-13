package com.example.mitalk.global.security.auth

import com.example.mitalk.domain.customer.domain.repository.CustomerRepository
import com.example.mitalk.domain.customer.exception.CustomerNotFoundException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AuthDetailsService(
        private val customerRepository: CustomerRepository
): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = customerRepository.findByEmail(username) ?: throw CustomerNotFoundException()
        return AuthDetails(user)
    }
}