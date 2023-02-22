package com.example.mitalk.domain.customer.presentation.data.request

import com.example.mitalk.domain.customer.domain.ReviewItem
import java.util.UUID

class ReviewRequest(
        val star: Int?,
        val message: String?,
        val reviewItem: List<ReviewItem>,
        val counsellor: UUID?
) {
}