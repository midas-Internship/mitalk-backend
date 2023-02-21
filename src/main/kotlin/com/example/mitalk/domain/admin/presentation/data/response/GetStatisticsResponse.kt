package com.example.mitalk.domain.admin.presentation.data.response

import java.util.UUID

data class GetStatisticsResponse(
    val allStatistics: AllStatistic,
    val counsellorStatistics: List<CounsellorStatics>
) {
    data class AllStatistic(
        val star: Double,
        val reviews: List<StatisticReviewElement>
    )

    data class CounsellorStatics(
        val name: String,
        val id: UUID,
        val star: Double
    )
}