package com.example.mitalk.global.security.auth

import com.example.mitalk.domain.auth.domain.Role
import com.example.mitalk.domain.counsellor.domain.entity.Counsellor
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CounsellorDetails(
        private val counsellor: Counsellor
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf(SimpleGrantedAuthority(Role.COUNSELLOR.name))

    override fun getPassword(): String? = null

    override fun getUsername(): String = counsellor.id.toString()

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true


}