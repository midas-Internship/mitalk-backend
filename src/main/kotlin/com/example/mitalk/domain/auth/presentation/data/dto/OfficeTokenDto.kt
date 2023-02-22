package com.example.mitalk.domain.auth.presentation.data.dto

import com.example.mitalk.domain.auth.domain.Role
import java.util.UUID

class OfficeTokenDto(
        val uuid: UUID,

        val role: Role,

        val accessToken: String,

        val refreshToken: String
) {
}