package com.example.mitalk.global.security.jwt

import com.example.mitalk.domain.auth.domain.Role
import com.example.mitalk.global.security.auth.CounselorDetailService
import com.example.mitalk.global.security.auth.CustomerDetailService
import com.example.mitalk.global.security.exception.ExpiredTokenException
import com.example.mitalk.global.security.exception.InvalidTokenException
import com.example.mitalk.global.security.jwt.properties.JwtProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.security.Key
import java.time.ZonedDateTime
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component
class JwtTokenProvider(
        private val jwtProperties: JwtProperties,
        private val customDetailService: CustomerDetailService,
        private val counselorDetailService: CounselorDetailService
) {
    companion object {
        const val ACCESS_TYPE = "access"
        const val REFRESH_TYPE = "refresh"
        const val ACCESS_EXP = 60L * 15 // 15 min
        const val REFRESH_EXP = 60L * 60 * 24 * 7 // 1 week
        const val TOKEN_PREFIX = "Bearer "
        const val AUTHORITY = "authority"
    }

    val accessExpiredTime: ZonedDateTime
        get() = ZonedDateTime.now().plusSeconds(ACCESS_EXP)

    val refreshExpiredTime: ZonedDateTime
        get() = ZonedDateTime.now().plusSeconds(REFRESH_EXP)

    fun generateAccessToken(email: String, role: Role): String =
            generateToken(email, ACCESS_TYPE, jwtProperties.accessSecret, ACCESS_EXP, role)

    fun generateRefreshToken(email: String, role: Role): String =
            generateToken(email, REFRESH_TYPE, jwtProperties.refreshSecret, REFRESH_EXP, role)

    fun resolveToken(req: HttpServletRequest): String? {
        val token = req.getHeader(HttpHeaders.AUTHORIZATION) ?: return null
        return parseToken(token)
    }

    fun exactEmailFromRefreshToken(refresh: String): String {
        return getTokenSubject(refresh, jwtProperties.refreshSecret)
    }

    fun authentication(token: String): Authentication {
        val userDetails = getLoadByUserDetail(token)
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun parseToken(token: String): String? =
            if (token.startsWith(TOKEN_PREFIX)) token.replace(TOKEN_PREFIX, "") else null

    fun generateToken(email: String, type: String, secret: Key, exp: Long, role: Role): String {
        val claims = Jwts.claims().setSubject(email)
        claims["type"] = type
        claims[AUTHORITY] = role
        return Jwts.builder()
                .signWith(secret, SignatureAlgorithm.HS256)
                .setClaims(claims)
                .setIssuedAt(Date())
                .setExpiration(Date(System.currentTimeMillis() + exp * 1000))
                .compact()
    }

    private fun getTokenBody(token: String, secret: Key): Claims {
        return try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token)
                    .body
        } catch (e: ExpiredJwtException) {
            throw ExpiredTokenException()
        } catch (e: Exception) {
            throw InvalidTokenException()
        }
    }

    private fun getTokenSubject(token: String, secret: Key): String =
            getTokenBody(token, secret).subject

    private fun getLoadByUserDetail(token: String): UserDetails {
        return when (getTokenBody(token, jwtProperties.accessSecret).get(AUTHORITY, String::class.java)) {
            Role.CUSTOMER.name -> customDetailService.loadUserByUsername(getTokenSubject(token, jwtProperties.accessSecret))
            Role.COUNSELLOR.name -> counselorDetailService.loadUserByUsername(getTokenSubject(token, jwtProperties.accessSecret))
            else -> throw TODO("유효하지 않은 토큰")
        }
    }

    fun socketAuthentication(parsedToken: String) : Claims {
        //TODO counsellor일시 처리
        return getTokenBody(parsedToken, jwtProperties.accessSecret)
    }
}