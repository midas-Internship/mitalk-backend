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
        val role: String = jwtTokenProvider.exactRoleFromRefreshToken(refresh)
        val roleEunm = getRoleEunm(role)
        val email: String = jwtTokenProvider.exactEmailFromRefreshToken(refresh)
        val existingRefreshToken = refreshTokenRepository.findByToken(refresh)
                ?: throw ExpiredRefreshTokenException()
        val newAccessToken = jwtTokenProvider.generateAccessToken(email, roleEunm)
        val newRefreshToken = jwtTokenProvider.generateRefreshToken(email, roleEunm)
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
                role = role,
                accessExp = accessExp,
                refreshExp = refreshExp
        )
    }

    private fun getRoleEunm(role: String): Role =
        when(role) {
            Role.CUSTOMER.name -> Role.CUSTOMER
            Role.COUNSELLOR.name -> Role.COUNSELLOR
            Role.ADMIN.name -> Role.ADMIN
            else -> Role.CUSTOMER
        }
}
