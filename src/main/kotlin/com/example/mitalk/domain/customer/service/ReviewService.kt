package com.example.mitalk.domain.customer.service

import com.example.mitalk.domain.customer.domain.entity.Review
import com.example.mitalk.domain.customer.domain.entity.ReviewElement
import com.example.mitalk.domain.customer.domain.repository.CustomerRepository
import com.example.mitalk.domain.customer.domain.repository.ReviewElementRepository
import com.example.mitalk.domain.customer.domain.repository.ReviewRepository
import com.example.mitalk.domain.customer.presentation.data.request.ReviewRequest
import com.example.mitalk.global.util.CustomerUtil
import org.springframework.stereotype.Service

@Service
class ReviewService(
        private val reviewRepository: ReviewRepository,
        private val reviewElementRepository: ReviewElementRepository,
        private val customerUtil: CustomerUtil,
        private val customerRepository: CustomerRepository,
) {
    fun execute(requestDto: ReviewRequest) {
        if (requestDto.star != null && requestDto.message != null) {
            val review = Review(0, requestDto.star, requestDto.message, requestDto.counsellor)
            val reviewId = reviewRepository.save(review).id

            reviewElementRepository.saveAll(
                    requestDto.reviewItem.map {
                        ReviewElement(ReviewElement.ReviewElementId(it, reviewId), review)
                    }
            )
        }

        val customer = customerUtil.getCurrentCustomer()
        customer.needReview = null

        customerRepository.save(customer)
    }
}