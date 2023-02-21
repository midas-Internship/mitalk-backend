package com.example.mitalk.domain.admin.service

import com.example.mitalk.domain.counsellor.domain.repository.CounsellorRepository
import com.example.mitalk.domain.counsellor.exception.CounsellorNotFoundException
import com.example.mitalk.global.socket.util.SessionUtils
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class DeleteCounsellorService(
        private val counsellorRepository: CounsellorRepository,
        private val sessionUtils: SessionUtils
) {
    @Transactional
    fun execute(deleteCounsellorRequest: String) {
        val counsellor = counsellorRepository.findByIdOrNull(UUID.fromString(deleteCounsellorRequest)) ?: throw CounsellorNotFoundException()
        if(counsellor.counsellorSession == null) {
            counsellorRepository.delete(counsellor)
        } else {
            sessionUtils.remove(counsellor.counsellorSession)
            counsellorRepository.delete(counsellor)
        }
    }
}