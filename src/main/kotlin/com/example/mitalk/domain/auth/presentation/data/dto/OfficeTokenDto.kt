package com.example.mitalk.domain.auth.presentation.data.dto

import java.util.UUID

class OfficeTokenDto(
        val uuid: UUID,

        val accessToken: String,

        val refreshToken: String
) {
}