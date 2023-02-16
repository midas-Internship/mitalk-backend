package com.example.mitalk.domain.auth.presentation

import com.example.mitalk.domain.auth.presentation.data.request.SignInOfficeRequest
import com.example.mitalk.domain.auth.presentation.data.response.NewRefreshTokenResponse
import com.example.mitalk.domain.auth.service.GetNewRefreshTokenService
import com.example.mitalk.domain.auth.service.impl.SignInOfficeService
import com.example.mitalk.domain.customer.presentation.data.response.SignInResponseDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
        private val getNewRefreshTokenService: GetNewRefreshTokenService,
        private val officeService: SignInOfficeService
) {

    @PatchMapping
    fun getNewRefreshToken(@RequestHeader("Refresh-Token") refreshToken: String): NewRefreshTokenResponse {
        return getNewRefreshTokenService.execute(refreshToken)
    }

    @PostMapping("/signin")
    fun signIn(@RequestBody requestDto: SignInOfficeRequest): SignInResponseDto {
        return officeService.execute(requestDto)
    }
}