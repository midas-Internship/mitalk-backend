package com.example.mitalk.global.socket.message

import java.util.UUID

class ChatMessage(
    val roomId: UUID,
    val role: MessageRole,
    val message: String
)

enum class MessageRole {
    CUSTOMER, COUNSELLOR
}