package com.example.mitalk.domain.customer.service

import com.example.mitalk.domain.customer.presentation.data.request.SignInRequest
import com.example.mitalk.domain.customer.presentation.data.response.SignInResponseDto

interface SignInService {
    fun execute(requestDto: SignInRequest): SignInResponseDto
}