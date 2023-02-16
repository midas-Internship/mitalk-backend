package com.example.mitalk.domain.admin.service

import com.example.mitalk.domain.admin.presentation.data.response.FindAllCounsellorResponse
import com.example.mitalk.domain.counsellor.domain.repository.CounsellorRepository
import org.springframework.stereotype.Service

@Service
class FindAllCounsellorService(
        private val counsellorRepository: CounsellorRepository
) {
    fun execute(): List<FindAllCounsellorResponse> {
        return counsellorRepository.findAll()
                .map { FindAllCounsellorResponse(
                        counsellorId = it.id,
                        roomId = it.roomId,
                        name = it.name,
                        counsellorSession = it.counsellorSession,
                        customerSession = it.customerSession,
                        todayCounsellingCount = it.todayCounsellingCount,
                        status = it.status
                ) }
    }
}