package com.example.mitalk.domain.admin.presentation.data.response

data class GetStatisticsDetailResponse(
    val reviews: List<StatisticReviewElement>,
    val messages: List<String>
)
