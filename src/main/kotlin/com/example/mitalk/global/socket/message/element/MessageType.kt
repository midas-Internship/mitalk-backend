package com.example.mitalk.global.socket.message.element

enum class MessageType(message: String) {

    SYSTEM_1_1_1("대기열 접속 성공"),
    SYSTEM_1_1_2("대기열 접속 실패 (꽉참)"),

    SYSTEM_3_1("상담 시작");

    fun message(): String = message()
}