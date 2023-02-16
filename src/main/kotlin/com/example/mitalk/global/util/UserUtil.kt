package com.example.mitalk.global.util

import com.example.mitalk.domain.counsellor.domain.entity.Counsellor
<<<<<<< Updated upstream
import com.example.mitalk.domain.counsellor.domain.repository.CounsellorRepository
import com.example.mitalk.domain.counsellor.exception.CounsellorNotFoundException
=======
>>>>>>> Stashed changes
import com.example.mitalk.domain.customer.domain.entity.Customer
import com.example.mitalk.domain.customer.domain.repository.CustomerRepository
import com.example.mitalk.domain.customer.exception.CustomerNotFoundException
import com.example.mitalk.global.security.auth.CounsellorDetails
import com.example.mitalk.global.security.auth.CustomerDetails
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserUtil(
        private val customerRepository: CustomerRepository,
        private val counsellorRepository: CounsellorRepository
) {
    fun getCurrentCustomer(): Customer {
        val principal = SecurityContextHolder.getContext().authentication.principal as CustomerDetails
        val email = principal.username
        return customerRepository.findByEmail(email) ?: throw CustomerNotFoundException()
    }

    fun getCurrentCounsellor(): Counsellor {
        val principal = SecurityContextHolder.getContext().authentication.principal as CounsellorDetails
        val uuid = principal.username
        return counsellorRepository.findByIdOrNull(UUID.fromString(uuid)) ?: throw CounsellorNotFoundException()
    }

    fun getCounsellorCustomer(): Counsellor {
        val principal = SecurityContextHolder.getContext().authentication.principal as CounsellorDetails
        val email = principal.username
        return customerRepository.findByEmail(email) ?: TODO("UserNotfound:")
    }
}