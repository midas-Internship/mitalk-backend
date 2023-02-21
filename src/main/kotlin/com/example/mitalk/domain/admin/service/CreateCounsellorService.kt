package com.example.mitalk.domain.admin.service

import com.example.mitalk.domain.admin.presentation.data.request.CreateCounsellorRequest
import com.example.mitalk.domain.admin.presentation.data.response.CreateCounsellorResponse
import com.example.mitalk.domain.counsellor.domain.entity.Counsellor
import com.example.mitalk.domain.counsellor.domain.repository.CounsellorRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateCounsellorService(
        private val counsellorRepository: CounsellorRepository
) {
    @Transactional
    fun execute(createCounsellorRequest: CreateCounsellorRequest): CreateCounsellorResponse {
        val counsellor = Counsellor(createCounsellorRequest.name)

        val counsellorId = counsellorRepository.save(counsellor).id
        return CreateCounsellorResponse(counsellorId!!)
    }


}