package com.example.mitalk.global.security.auth

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CounselorDetailService(
        //TODO counselorRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val user = TODO( "counselorRepository.findByEmail(username) ?: throw CustomerNotFoundException()")
        return CustomerDetails(user)
    }
}