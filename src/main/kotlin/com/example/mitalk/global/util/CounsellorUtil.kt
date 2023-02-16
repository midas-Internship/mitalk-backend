package com.example.mitalk.global.util

import com.example.mitalk.domain.counsellor.domain.entity.Counsellor
import com.example.mitalk.domain.counsellor.domain.repository.CounsellorRepository
import com.example.mitalk.domain.counsellor.exception.CounsellorNotFoundException
import com.example.mitalk.global.security.auth.CounsellorDetails
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.util.*

@Component
class CounsellorUtil(
        private val counsellorRepository: CounsellorRepository
) {
    fun getCurrentCounsellor(): Counsellor {
        val principal = SecurityContextHolder.getContext().authentication.principal as CounsellorDetails
        val uuid = principal.username
        return counsellorRepository.findByIdOrNull(UUID.fromString(uuid)) ?: throw CounsellorNotFoundException()
    }
}