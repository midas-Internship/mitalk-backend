package com.example.mitalk.global.socket.message

import com.example.mitalk.domain.auth.domain.Role
import java.util.UUID

class ChatMessage(
    val roomId: UUID,
    val role: Role,
    val message: String
)