package com.example.mitalk.domain.auth.presentation.data.response

import java.time.ZonedDateTime

class NewRefreshTokenResponse(
        val accessToken: String,
        val refreshToken: String,
        val accessExp: ZonedDateTime,
        val refreshExp: ZonedDateTime
)