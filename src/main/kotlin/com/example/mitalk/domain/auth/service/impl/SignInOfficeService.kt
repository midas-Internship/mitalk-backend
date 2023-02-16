package com.example.mitalk.domain.auth.service.impl

import com.example.mitalk.domain.admin.domain.repository.AdminRepository
import com.example.mitalk.domain.auth.domain.Role
import com.example.mitalk.domain.auth.domain.entity.RefreshToken
import com.example.mitalk.domain.auth.domain.repository.RefreshTokenRepository
import com.example.mitalk.domain.auth.presentation.data.dto.OfficeTokenDto
import com.example.mitalk.domain.auth.presentation.data.request.SignInOfficeRequest
import com.example.mitalk.domain.counsellor.domain.repository.CounsellorRepository
import com.example.mitalk.domain.customer.presentation.data.response.SignInResponseDto
import com.example.mitalk.global.security.jwt.JwtTokenProvider
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.util.*

@Service
class SignInOfficeService(
        private val jwtTokenProvider: JwtTokenProvider,
        private val counsellorRepository: CounsellorRepository,
        private val adminRepository: AdminRepository,
        private val refreshTokenRepository: RefreshTokenRepository
) {
    fun execute(requestDto: SignInOfficeRequest): SignInResponseDto {

        val officeToken = generatedOfficeToken(requestDto.id)
        val accessExp: ZonedDateTime = jwtTokenProvider.accessExpiredTime
        val refreshExp: ZonedDateTime = jwtTokenProvider.refreshExpiredTime

        saveNewRefreshToken(officeToken.uuid, officeToken.refreshToken)

        return SignInResponseDto(
                accessToken = officeToken.accessToken,
                refreshToken = officeToken.refreshToken,
                accessExp = accessExp,
                refreshExp = refreshExp
        )
    }

    private fun saveNewRefreshToken(id: UUID, refreshToken: String) {
        val refreshToken = RefreshToken(
                userId = id,
                token = refreshToken
        )
        refreshTokenRepository.save(refreshToken)
    }

    private fun generatedOfficeToken(id: UUID): OfficeTokenDto {
        val counsellor = counsellorRepository.findByIdOrNull(id)
        val admin = adminRepository.findByIdOrNull(id)
        var accessToken: String = ""
        var refreshToken: String = ""
        var uuid: UUID = UUID(0, 0)

        if(counsellor != null) {
            accessToken = jwtTokenProvider.generateAccessToken(counsellor.id.toString(), Role.COUNSELLOR)
            refreshToken = jwtTokenProvider.generateRefreshToken(counsellor.id.toString(), Role.COUNSELLOR)
            uuid = counsellor.id!!

        }
        if(admin != null) {
            accessToken = jwtTokenProvider.generateAccessToken(admin.id.toString(), Role.ADMIN)
            refreshToken = jwtTokenProvider.generateRefreshToken(admin.id.toString(), Role.ADMIN)
            uuid = admin.id!!
            return OfficeTokenDto(admin.id!!, accessToken, refreshToken)
        }
        return OfficeTokenDto(uuid, accessToken, refreshToken)
    }
}