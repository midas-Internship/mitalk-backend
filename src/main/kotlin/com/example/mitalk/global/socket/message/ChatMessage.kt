package com.example.mitalk.global.socket.message

import com.example.mitalk.domain.auth.domain.Role
import java.util.UUID

class ChatMessage(
    val roomId: UUID,
    val messageId: UUID,
    val role: Role,
    val chatMessageType: ChatMessageType,
    val message: String
) {
    enum class ChatMessageType {
        SEND, UPDATE, DELETE
    }
}
