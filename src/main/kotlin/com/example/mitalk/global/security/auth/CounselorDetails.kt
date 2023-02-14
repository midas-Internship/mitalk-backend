package com.example.mitalk.global.security.auth

import com.example.mitalk.global.socket.dto.Role
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CounselorDetails(
        //TODO("")
) : UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf(SimpleGrantedAuthority(Role.CUSTOMER.name))

    override fun getPassword(): String? = null

    override fun getUsername(): String = TODO("counselor.email")

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}