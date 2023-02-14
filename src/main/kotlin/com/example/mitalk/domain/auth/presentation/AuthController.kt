package com.example.mitalk.domain.auth.presentation

import com.example.mitalk.domain.auth.presentation.data.response.NewRefreshTokenResponse
import com.example.mitalk.domain.auth.service.GetNewRefreshTokenService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
        private val getNewRefreshTokenService: GetNewRefreshTokenService
) {

    @PatchMapping
    fun getNewRefreshToken(@RequestHeader("Refresh-Token") refreshToken: String): NewRefreshTokenResponse {
        return getNewRefreshTokenService.execute(refreshToken)
    }
}