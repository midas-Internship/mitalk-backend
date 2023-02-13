package com.example.mitalk.domain.customer.service

import com.example.mitalk.domain.customer.presentation.data.request.SignInRequest

interface SignInService {
    fun execute(requestDto: SignInRequest)
}