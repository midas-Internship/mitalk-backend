package com.example.mitalk.global.socket.message.element

enum class MessageType(
    private val message: String
) {

    SYSTEM_1_1_1("대기열 접속 성공"),
    SYSTEM_1_1_2("대기열 접속 실패 (꽉참)"),
    SYSTEM_1_2("현재 대기열"),

    SYSTEM_3_1("상담 시작"),
    SYSTEM_3_2("방 터짐");
    fun message(): String = message
}