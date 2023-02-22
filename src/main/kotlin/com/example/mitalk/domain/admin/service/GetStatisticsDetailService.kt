package com.example.mitalk.domain.admin.service

import com.example.mitalk.domain.admin.presentation.data.response.GetStatisticsDetailResponse
import com.example.mitalk.domain.admin.presentation.data.response.StatisticReviewElement
import com.example.mitalk.domain.customer.domain.repository.ReviewRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class GetStatisticsDetailService(
    private val reviewRepository: ReviewRepository
) {

    fun execute(counsellorId: UUID): GetStatisticsDetailResponse {
        return GetStatisticsDetailResponse(
            reviews = reviewRepository.getReviewByCounsellorId(counsellorId).map {
                StatisticReviewElement(
                    reviewItem = it.get(0, String::class.java),
                    percentage = it.get(1, Double::class.java)
                )
            },
            messages = reviewRepository.findByCounsellor(counsellorId).map { it.message!! }
        )
    }
}