package com.example.mitalk.domain.admin.presentation.data.response

import com.example.mitalk.domain.customer.domain.ReviewItem

data class StatisticReviewElement(
    val reviewItem: ReviewItem,
    val percentage: Double
)