package com.example.mitalk.domain.customer.presentation.data.response

import java.time.ZonedDateTime

class SignInResponseDto(
        val accessToken: String,
        val refreshToken: String,
        val accessExp: ZonedDateTime,
        val refreshExp: ZonedDateTime

) {
}