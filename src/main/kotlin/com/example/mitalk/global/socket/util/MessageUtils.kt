package com.example.mitalk.global.socket.util

import com.example.mitalk.global.socket.message.ChatMessage
import com.example.mitalk.global.socket.message.element.SystemMessage
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession

@Component
class MessageUtils(
    private val mapper: ObjectMapper
) {
    fun sendSystemMessage(message: SystemMessage, session: WebSocketSession) {
        println("4.2 ")
        session.sendMessage(TextMessage(mapper.writeValueAsString(message)))
        println("4.442")
    }

    fun sendChatMessage(message: ChatMessage, customerSession: WebSocketSession, counsellorSession: WebSocketSession) {
        val textMessage = TextMessage(mapper.writeValueAsString(message))
        customerSession.sendMessage(textMessage)
        counsellorSession.sendMessage(textMessage)
    }
}