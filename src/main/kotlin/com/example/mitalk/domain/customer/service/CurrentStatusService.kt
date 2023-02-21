package com.example.mitalk.domain.customer.service

import com.example.mitalk.domain.counsellor.domain.repository.CounsellorRepository
import com.example.mitalk.domain.customer.presentation.data.response.CurrentStatusResponse
import com.example.mitalk.global.util.UserUtil
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CurrentStatusService(
        private val counselorRepository: CounsellorRepository,
        private val userUtil: UserUtil
) {
    @Transactional(readOnly = true)
    fun execute(): CurrentStatusResponse {
        val needReview = userUtil.getCurrentCustomer().needReview
                ?: return CurrentStatusResponse(
                        needReview = null,
                        name = null
                )
        val counsellor = counselorRepository.findByIdOrNull(needReview) ?: TODO("상담원 이 존재하지 않습니다. Exception")

        return CurrentStatusResponse(
                needReview = needReview,
                name = counsellor.name
        )
    }
}