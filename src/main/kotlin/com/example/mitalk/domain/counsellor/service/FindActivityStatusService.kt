package com.example.mitalk.domain.counsellor.service

import com.example.mitalk.domain.counsellor.domain.entity.CounsellorStatus
import com.example.mitalk.domain.counsellor.presentation.data.response.FindActivityStatusResponse
import com.example.mitalk.global.util.UserUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FindActivityStatusService(
        private val userUtil: UserUtil
) {
    @Transactional(readOnly = true)
    fun execute(): FindActivityStatusResponse {
        val counsellor = userUtil.getCurrentCounsellor()
        if (counsellor.status == CounsellorStatus.ONLINE) {
            return FindActivityStatusResponse(true)
        } else {
            return FindActivityStatusResponse(false)
        }
    }
}