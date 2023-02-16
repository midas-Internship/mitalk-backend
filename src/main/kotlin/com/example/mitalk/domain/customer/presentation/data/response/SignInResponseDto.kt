package com.example.mitalk.domain.customer.presentation.data.response

import com.example.mitalk.domain.auth.domain.Role
import java.time.ZonedDateTime

class SignInResponseDto(
        val accessToken: String,
        val refreshToken: String,
        val role: Role,
        val accessExp: ZonedDateTime,
        val refreshExp: ZonedDateTime
) {
}