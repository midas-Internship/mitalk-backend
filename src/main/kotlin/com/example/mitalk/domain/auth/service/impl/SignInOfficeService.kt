package com.example.mitalk.domain.auth.service.impl

import com.example.mitalk.domain.admin.domain.repository.AdminRepository
import com.example.mitalk.domain.auth.domain.Role
import com.example.mitalk.domain.auth.domain.entity.RefreshToken
import com.example.mitalk.domain.auth.domain.repository.RefreshTokenRepository
import com.example.mitalk.domain.auth.exception.OfficialsNotFoundException
import com.example.mitalk.domain.auth.presentation.data.dto.OfficeTokenDto
import com.example.mitalk.domain.auth.presentation.data.request.SignInOfficeRequest
import com.example.mitalk.domain.counsellor.domain.repository.CounsellorRepository
import com.example.mitalk.domain.customer.presentation.data.response.SignInResponseDto
import com.example.mitalk.global.security.jwt.JwtTokenProvider
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.lang.RuntimeException
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

        val officeToken = issueOfficeTokenToAccordingAuthority(requestDto.id)
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

    private fun issueOfficeTokenToAccordingAuthority(id: UUID): OfficeTokenDto {
        val counsellor = counsellorRepository.findByIdOrNull(id)
        val admin = adminRepository.findByIdOrNull(id)

        if(counsellor != null) {
            generatedOfficeToken(counsellor.id)
        }
        else if(admin != null) {
            return generatedOfficeToken(admin.id)
        }

        return throw OfficialsNotFoundException()
    }

    private fun generatedOfficeToken(uuid: UUID): OfficeTokenDto {
        val accessToken = jwtTokenProvider.generateAccessToken(uuid.toString(), Role.COUNSELLOR)
        val refreshToken = jwtTokenProvider.generateRefreshToken(uuid.toString(), Role.COUNSELLOR)
        val uuid = uuid
        return OfficeTokenDto(uuid, accessToken, refreshToken)

    }
}