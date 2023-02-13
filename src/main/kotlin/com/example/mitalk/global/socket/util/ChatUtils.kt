package com.example.mitalk.global.socket.util

import com.example.mitalk.global.socket.message.element.Message
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

@Component
class ChatUtils(
    private val mapper: ObjectMapper
) {
    fun sendMessage(message: Message, session: WebSocketSession) {
        session.sendMessage(TextMessage(mapper.writeValueAsString(message)))
    }
}