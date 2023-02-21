package com.example.mitalk.domain.auth.presentation.data.response

import com.example.mitalk.domain.auth.domain.Role
import java.time.ZonedDateTime
import java.util.UUID

class SignInOfficeResponse(
        val accessToken: String,
        val refreshToken: String,
        val role: Role,
        val uuid: UUID,
        val accessExp: ZonedDateTime,
        val refreshExp: ZonedDateTime
) {
}