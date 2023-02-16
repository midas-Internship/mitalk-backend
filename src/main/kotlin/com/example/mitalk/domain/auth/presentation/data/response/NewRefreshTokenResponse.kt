package com.example.mitalk.domain.auth.presentation.data.response

import com.example.mitalk.domain.auth.domain.Role
import java.time.ZonedDateTime

class NewRefreshTokenResponse(
        val accessToken: String,
        val refreshToken: String,
        val role: String,
        val accessExp: ZonedDateTime,
        val refreshExp: ZonedDateTime
)