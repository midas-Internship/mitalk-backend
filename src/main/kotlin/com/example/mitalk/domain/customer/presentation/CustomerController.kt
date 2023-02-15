package com.example.mitalk.domain.customer.presentation

import com.example.mitalk.domain.customer.presentation.data.request.ReviewRequest
import com.example.mitalk.domain.customer.presentation.data.request.SignInRequest
import com.example.mitalk.domain.customer.presentation.data.response.CurrentStatusResponse
import com.example.mitalk.domain.customer.presentation.data.response.SignInResponseDto
import com.example.mitalk.domain.customer.service.ReviewService
import com.example.mitalk.domain.customer.service.SignInService
import com.example.mitalk.global.util.UserUtil
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/customer")
class CustomerController(
        private val signInService: SignInService,
        private val userUtil: UserUtil,
        private val reviewService: ReviewService
) {

    @PostMapping("/signin")
    fun signIn(@RequestBody signInRequest: SignInRequest): ResponseEntity<SignInResponseDto> =
            signInService.execute(signInRequest)
                    .let { ResponseEntity.ok(it) }


    @GetMapping("/review")
    fun currentStatus(): CurrentStatusResponse = CurrentStatusResponse(userUtil.getCurrentCustomer().needReview)

    @PostMapping("/review")
    fun createReview(@RequestBody reviewRequest: ReviewRequest) {
        reviewService.execute(reviewRequest)
    }
}