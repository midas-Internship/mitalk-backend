package com.example.mitalk.domain.auth.service

import com.example.mitalk.domain.auth.presentation.data.response.NewRefreshTokenResponse

interface GetNewRefreshTokenService {
    fun execute(refreshToken: String): NewRefreshTokenResponse
}