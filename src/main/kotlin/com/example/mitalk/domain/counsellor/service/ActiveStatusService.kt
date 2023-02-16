package com.example.mitalk.domain.counsellor.service

import com.example.mitalk.domain.counsellor.domain.entity.Counsellor
import com.example.mitalk.domain.counsellor.domain.entity.CounsellorStatus
import com.example.mitalk.domain.counsellor.domain.repository.CounsellorRepository
import com.example.mitalk.domain.counsellor.presentation.data.request.ActivityStatusRequest
import com.example.mitalk.domain.counsellor.presentation.data.response.ActivityStatusResponse
import com.example.mitalk.global.util.CounsellorUtil
import org.springframework.stereotype.Service

@Service
class ActiveStatusService(
        private val counsellorRepository: CounsellorRepository,
        private val counsellorUtil: CounsellorUtil
) {
    fun execute(activityStatusRequest: ActivityStatusRequest): ActivityStatusResponse {
        val counsellor: Counsellor = counsellorUtil.getCurrentCounsellor()

        if(activityStatusRequest.isActivity) {
            counsellor.status = CounsellorStatus.ONLINE
            counsellorRepository.save(counsellor)
            return ActivityStatusResponse(true)
        } else {
            counsellor.status = CounsellorStatus.OFFLINE
            counsellorRepository.save(counsellor)
            return ActivityStatusResponse(false)
        }
    }
}