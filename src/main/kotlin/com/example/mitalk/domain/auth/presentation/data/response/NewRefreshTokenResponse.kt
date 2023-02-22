package com.example.mitalk.domain.auth.presentation.data.response

import com.example.mitalk.domain.auth.domain.Role
import java.time.ZonedDateTime
import java.util.UUID

class NewRefreshTokenResponse(
        val accessToken: String,
        val refreshToken: String,
        val uuid: UUID,
        val role: String,
        val accessExp: ZonedDateTime,
        val refreshExp: ZonedDateTime
)