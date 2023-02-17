package com.example.mitalk.domain.admin.presentation.data.response

import java.util.UUID

data class GetCustomerListResponse(
    val id: UUID,
    val name: String,
    val email: String,
    val session: UUID?
)