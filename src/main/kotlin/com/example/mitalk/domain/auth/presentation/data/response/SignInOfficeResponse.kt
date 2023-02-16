package com.example.mitalk.domain.auth.presentation.data.response

import com.example.mitalk.domain.auth.domain.Role
import java.time.ZonedDateTime

class SignInOfficeResponse(
        val accessToken: String,
        val refreshToken: String,
        val role: Role,
        val accessExp: ZonedDateTime,
        val refreshExp: ZonedDateTime
) {
}