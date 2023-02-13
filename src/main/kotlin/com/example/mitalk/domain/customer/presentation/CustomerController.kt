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
class CustomController(
    private val signInService: SignInService
) {

//    @PostMapping("/user/{userId}")
//    fun saveUserSession(@PathVariable("userId") userId: String) {
//        customerSessionRepository.save(CustomerSession(userId))
//
//
//    }

    @PostMapping("/login")
    fun signIn(@RequestBody() signInRequest: SignInRequest): ResponseEntity<SignInResponseDto> =
        signInService.execute(signInRequest)
            .let { ResponseEntity.ok(it) }
}