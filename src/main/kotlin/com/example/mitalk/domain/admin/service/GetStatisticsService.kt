package com.example.mitalk.domain.admin.service

import com.example.mitalk.domain.admin.presentation.data.response.GetStatisticsResponse
import com.example.mitalk.domain.counsellor.domain.repository.CounsellorRepository
import com.example.mitalk.domain.customer.domain.repository.ReviewRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetStatisticsService(
    private val reviewRepository: ReviewRepository,
    private val counsellorRepository: CounsellorRepository
) {
    @Transactional(readOnly = true)
    fun execute(): GetStatisticsResponse {
        return GetStatisticsResponse(
            allStatistics = GetStatisticsResponse.AllStatistic(
                star = reviewRepository.getAllStar(),
                reviews = reviewRepository.getAllReviews()
            ),
            counsellorStatistics = reviewRepository.findAll().map {
                GetStatisticsResponse.CounsellorStatics(
                    name = (counsellorRepository.findByIdOrNull(it.counsellor) ?: TODO("Counsellor Not Found E")).name,
                    id = it.counsellor!!,
                    star = reviewRepository.getStarByCounsellorId(it.counsellor)
                )
            }
        )
    }
}