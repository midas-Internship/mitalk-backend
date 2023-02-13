package com.example.mitalk.domain.customer.service.impl

import com.example.mitalk.domain.customer.domain.repository.CustomerRepository
import com.example.mitalk.domain.customer.exception.CustomerNotFoundException
import com.example.mitalk.domain.customer.presentation.data.request.SignInRequest
import com.example.mitalk.domain.customer.presentation.data.response.SignInResponseDto
import com.example.mitalk.domain.customer.service.SignInService
import com.example.mitalk.domain.customer.util.CustomerUtil
import com.example.mitalk.global.security.jwt.JwtTokenProvider
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class SignInServiceImpl(
    private val jwtTokenProvider: JwtTokenProvider,
    private val customerRepository: CustomerRepository,
    private val customerUtil: CustomerUtil
) : SignInService {
    override fun execute(requestDto: SignInRequest): SignInResponseDto {
        val customer = customerRepository.findByEmail(requestDto.email)
        val accessToken: String = jwtTokenProvider.generateAccessToken(requestDto.email)
        val refreshToken: String = jwtTokenProvider.generateRefreshToken(requestDto.email)
        val accessExp: ZonedDateTime = jwtTokenProvider.accessExpiredTime
        val refreshExp: ZonedDateTime = jwtTokenProvider.refreshExpiredTime

        if (customer == null) {
            customerUtil.saveNewCustomer(requestDto, refreshToken)
        } else {
            customerUtil.saveNewRefreshToken(customer, refreshToken)
        }

        return SignInResponseDto(
            accessToken = accessToken,
            refreshToken = refreshToken,
            accessExp = accessExp,
            refreshExp = refreshExp
        )
    }
}