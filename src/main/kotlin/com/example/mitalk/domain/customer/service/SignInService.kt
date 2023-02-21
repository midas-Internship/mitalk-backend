package com.example.mitalk.domain.customer.service

import com.example.mitalk.domain.auth.domain.Role
import com.example.mitalk.domain.customer.domain.repository.CustomerRepository
import com.example.mitalk.domain.customer.presentation.data.request.SignInRequest
import com.example.mitalk.domain.customer.presentation.data.response.SignInResponseDto
import com.example.mitalk.domain.customer.util.CustomerGeneratedUtil
import com.example.mitalk.global.security.jwt.JwtTokenProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime

@Service
class SignInService(
    private val jwtTokenProvider: JwtTokenProvider,
    private val customerRepository: CustomerRepository,
    private val customerGeneratedUtil: CustomerGeneratedUtil
) {
    @Transactional
    fun execute(requestDto: SignInRequest): SignInResponseDto {

        val customer = customerRepository.findByEmail(requestDto.email)

        val accessToken: String = jwtTokenProvider.generateAccessToken(requestDto.email, Role.CUSTOMER)
        val refreshToken: String = jwtTokenProvider.generateRefreshToken(requestDto.email, Role.CUSTOMER)
        val accessExp: ZonedDateTime = jwtTokenProvider.accessExpiredTime
        val refreshExp: ZonedDateTime = jwtTokenProvider.refreshExpiredTime

        if (customer == null) {
            customerGeneratedUtil.saveNewCustomer(requestDto, refreshToken)
        } else {
            customerGeneratedUtil.saveNewRefreshToken(customer, refreshToken)
        }

        return SignInResponseDto(
                accessToken = accessToken,
                refreshToken = refreshToken,
                role = Role.CUSTOMER,
                uuid = customer!!.id,
                accessExp = accessExp,
                refreshExp = refreshExp
        )
    }
}