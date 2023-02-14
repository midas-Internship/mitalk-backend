package com.example.mitalk.domain.customer.presentation

import com.example.mitalk.domain.customer.presentation.data.request.SignInRequest
import com.example.mitalk.domain.customer.presentation.data.response.SignInResponseDto
import com.example.mitalk.domain.customer.service.SignInService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/customer")
class CustomerController(
    private val signInService: SignInService
) {

    @PostMapping("/signin")
    fun signIn(@RequestBody() signInRequest: SignInRequest): ResponseEntity<SignInResponseDto> =
        signInService.execute(signInRequest)
            .let { ResponseEntity.ok(it) }

    @PostMapping("/review")
    fun createReview() {

    }
}