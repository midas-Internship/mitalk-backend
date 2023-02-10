package com.example.mitalk.global.socket.handler

import com.example.mitalk.global.socket.ChatService
import com.example.mitalk.global.socket.dto.ChatMessage
import com.example.mitalk.global.socket.dto.Role
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import org.springframework.web.socket.BinaryMessage
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class SocketHandler(
    val mapper: ObjectMapper,
    val chatService: ChatService,
) : TextWebSocketHandler() {
    companion object {
        private var list: MutableList<WebSocketSession> = arrayListOf()
    }
    override fun afterConnectionEstablished(session: WebSocketSession) {
        session.attributes.forEach {
            println(session.attributes)
        }
        list.add(session)
        println("$session 클라이언트 접속")
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        println("$session 클라이언트 접속 해제")
        list.remove(session)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val payload = message.payload
        println("payload = $payload")
        val chatMessage = mapper.readValue(payload, ChatMessage::class.java)
        val room = chatService.findRoomById(chatMessage.roomId)
        if(room != null) {
            room.handleActions(session, chatMessage, chatService)
        } else println("room was not exist")
    }

    override fun handleBinaryMessage(session: WebSocketSession, message: BinaryMessage) {
    }
}