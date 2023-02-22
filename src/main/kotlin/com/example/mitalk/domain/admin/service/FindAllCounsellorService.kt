package com.example.mitalk.domain.admin.service

import com.example.mitalk.domain.admin.presentation.data.response.FindAllCounsellorResponse
import com.example.mitalk.domain.counsellor.domain.repository.CounsellorRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FindAllCounsellorService(
        private val counsellorRepository: CounsellorRepository
) {
    @Transactional(readOnly = true)
    fun execute(): List<FindAllCounsellorResponse> =
            counsellorRepository.findAll()
                    .map {
                        FindAllCounsellorResponse(
                                counsellorId = it.id,
                                name = it.name,
                                status = it.status
                        )
                    }
}