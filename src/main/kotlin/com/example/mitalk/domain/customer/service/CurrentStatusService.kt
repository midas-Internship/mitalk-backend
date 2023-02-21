package com.example.mitalk.domain.customer.service

import com.example.mitalk.domain.counsellor.domain.repository.CounsellorRepository
import com.example.mitalk.domain.customer.domain.repository.CustomerInfoRepository
import com.example.mitalk.domain.customer.presentation.data.response.CurrentStatusResponse
import com.example.mitalk.global.util.UserUtil
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class CurrentStatusService(
        private val counsellorRepository: CounsellorRepository,
        private val userUtil: UserUtil,
        private val customerInfoRepository: CustomerInfoRepository,
) {
    @Transactional(readOnly = true)
    fun execute(): CurrentStatusResponse {

        val user = userUtil.getCurrentCustomer()

        var needReview: UUID? = null
        var counsellorName: String? = null
        var roomId: UUID? = null

        if (user.needReview != null) {
            val counsellor = counsellorRepository.findByIdOrNull(user.needReview) ?: TODO("상담원 이 존재하지 않습니다. Exception")
            needReview = user.needReview
            counsellorName = counsellor.name
        }

        val customerInfo = customerInfoRepository.findByIdOrNull(user.id)
        if (customerInfo != null) {
            val counsellor = counsellorRepository.findByCustomerSession(customerInfo.customerSessionId) ?: TODO("상담원이 존재하지 않습니다. Exception")
            roomId = counsellor.roomId
        }

        return CurrentStatusResponse(
            needReview, counsellorName, roomId
        )
    }
}