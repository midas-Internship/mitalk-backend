package com.example.mitalk.domain.email.presentation.data.dto

import java.util.*

class EmailSentDto(
        val email: String,
        val customerSessionId: UUID
) {
}