package com.example.mitalk.domain.customer.presentation

import com.example.mitalk.domain.customer.presentation.data.request.ReviewRequest
import com.example.mitalk.domain.customer.presentation.data.request.SignInRequest
import com.example.mitalk.domain.customer.presentation.data.response.CurrentStatusResponse
import com.example.mitalk.domain.customer.presentation.data.response.QuestionListResponse
import com.example.mitalk.domain.customer.presentation.data.response.SignInResponseDto
import com.example.mitalk.domain.customer.service.CurrentStatusService
import com.example.mitalk.domain.customer.service.FindAllQuestionListService
import com.example.mitalk.domain.customer.service.ReviewService
import com.example.mitalk.domain.customer.service.SignInService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/customer")
class CustomerController(
        private val signInService: SignInService,
        private val currentStatusService: CurrentStatusService,
        private val reviewService: ReviewService,
        private val findAllQuestionListService: FindAllQuestionListService
) {

    @PostMapping("/signin")
    fun signIn(@RequestBody signInRequest: SignInRequest): ResponseEntity<SignInResponseDto> =
            signInService.execute(signInRequest)
                    .let { ResponseEntity.ok(it) }


    @GetMapping("/review")
    fun currentStatus(): CurrentStatusResponse = currentStatusService.execute()

    @PostMapping("/review")
    fun createReview(@RequestBody reviewRequest: ReviewRequest) {
        reviewService.execute(reviewRequest)
    }

    @GetMapping("/question")
    fun findAllQuestion(): List<QuestionListResponse> = findAllQuestionListService.execute()
}