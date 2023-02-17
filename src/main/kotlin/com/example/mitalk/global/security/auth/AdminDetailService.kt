package com.example.mitalk.global.security.auth

import com.example.mitalk.domain.admin.domain.repository.AdminRepository
import com.example.mitalk.domain.admin.exception.AdminNotFoundException
import com.example.mitalk.domain.counsellor.exception.CounsellorNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(readOnly = true)
class AdminDetailService(
        private val adminRepository: AdminRepository
) : UserDetailsService {
    override fun loadUserByUsername(uuid: String): UserDetails {
        val admin = adminRepository.findByIdOrNull(UUID.fromString(uuid)) ?: throw AdminNotFoundException()
        return AdminDetails(admin)
    }
}