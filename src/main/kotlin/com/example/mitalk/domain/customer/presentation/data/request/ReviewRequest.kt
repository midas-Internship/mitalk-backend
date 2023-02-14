package com.example.mitalk.domain.customer.presentation.data.request

class ReviewRequest(
        val star: Int,
        val message: String,
        val reviewItem: List<String>
) {
}