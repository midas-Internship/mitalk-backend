package com.example.mitalk.domain.auth.domain.repository

import com.example.mitalk.domain.auth.domain.entity.RefreshToken
import org.springframework.data.repository.CrudRepository
import java.util.*

interface RefreshTokenRepository : CrudRepository<RefreshToken, UUID> {
    fun findByToken(token: String): RefreshToken?
}