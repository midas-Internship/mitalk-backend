package com.example.mitalk.domain.admin.service

import com.example.mitalk.domain.admin.presentation.data.response.GetStatisticsDetailResponse
import com.example.mitalk.domain.customer.domain.repository.ReviewRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetStatisticsDetailService(
    private val reviewRepository: ReviewRepository
) {

    fun execute(counsellorId: UUID): GetStatisticsDetailResponse {
        return GetStatisticsDetailResponse(
            reviews = reviewRepository.getReviewByCounsellorId(counsellorId),
            messages = reviewRepository.findByCounsellor(counsellorId).map { it.message!! }
        )
    }
}