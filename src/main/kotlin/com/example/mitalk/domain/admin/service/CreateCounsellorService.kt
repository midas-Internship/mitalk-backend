package com.example.mitalk.domain.admin.service

import com.example.mitalk.domain.admin.presentation.data.request.CreateCounsellorRequest
import com.example.mitalk.domain.admin.presentation.data.response.CreateCounsellorResponse
import com.example.mitalk.domain.counsellor.domain.entity.Counsellor
import com.example.mitalk.domain.counsellor.domain.repository.CounsellorRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class CreateCounsellorService(
        private val counsellorRepository: CounsellorRepository
) {
    fun execute(createCounsellorRequest: CreateCounsellorRequest): CreateCounsellorResponse {
        val counsellor = generatedCounsellor(createCounsellorRequest.name)

        val counsellorId = counsellorRepository.save(counsellor).id
        return CreateCounsellorResponse(counsellorId!!)
    }

    private fun generatedCounsellor(name: String): Counsellor =
            Counsellor(
                    id = UUID.randomUUID(),
                    roomId = null,
                    name = name,
                    counsellorSession = null,
                    customerSession = null
            )
}