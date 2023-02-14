package com.example.mitalk.global.socket.dto

enum class Role(
        private val description: String
) {
    CUSTOMER("유저"),
    COUNSELLOR("상담사"),
    ADMIN("관리자");

    fun description() = description
}