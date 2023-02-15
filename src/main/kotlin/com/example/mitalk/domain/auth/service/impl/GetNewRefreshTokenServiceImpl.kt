package com.example.mitalk.domain.auth.service.impl

import com.example.mitalk.domain.auth.domain.Role
import com.example.mitalk.domain.auth.exception.ExpiredRefreshTokenException
import com.example.mitalk.domain.auth.presentation.data.response.NewRefreshTokenResponse
import com.example.mitalk.domain.auth.service.GetNewRefreshTokenService
import com.example.mitalk.domain.auth.domain.entity.RefreshToken
import com.example.mitalk.domain.auth.domain.repository.RefreshTokenRepository
import com.example.mitalk.global.security.exception.InvalidTokenException
import com.example.mitalk.global.security.jwt.JwtTokenProvider
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class GetNewRefreshTokenServiceImpl(
    private val jwtTokenProvider: JwtTokenProvider,
    private val refreshTokenRepository: RefreshTokenRepository,
) : GetNewRefreshTokenService {

    override fun execute(refreshToken: String): NewRefreshTokenResponse {
        val refresh = jwtTokenProvider.parseToken(refreshToken)
                ?: throw InvalidTokenException()
        val email: String = jwtTokenProvider.exactEmailFromRefreshToken(refresh)
        val existingRefreshToken = refreshTokenRepository.findByToken(refresh)
                ?: throw ExpiredRefreshTokenException()
        val newAccessToken = jwtTokenProvider.generateAccessToken(email, Role.CUSTOMER)
        val newRefreshToken = jwtTokenProvider.generateRefreshToken(email, Role.CUSTOMER)
        val accessExp: ZonedDateTime = jwtTokenProvider.accessExpiredTime
        val refreshExp: ZonedDateTime = jwtTokenProvider.refreshExpiredTime

        val newRefreshTokenEntity = RefreshToken(
                userId = existingRefreshToken.userId,
                token = newRefreshToken
        )
        refreshTokenRepository.save(newRefreshTokenEntity)

        return NewRefreshTokenResponse(
                accessToken = newAccessToken,
                refreshToken = newRefreshToken,
                accessExp = accessExp,
                refreshExp = refreshExp
        )
    }
}
