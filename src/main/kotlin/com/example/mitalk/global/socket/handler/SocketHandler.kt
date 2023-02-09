package com.example.mitalk.global.socket.handler

import org.springframework.stereotype.Component
import org.springframework.web.socket.BinaryMessage
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class SocketHandler : TextWebSocketHandler() {
    companion object {
        private var list: MutableList<WebSocketSession> = arrayListOf()
    }
    override fun afterConnectionEstablished(session: WebSocketSession) {
        list.add(session)
        println("$session 클라이언트 접속")
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        println("$session 클라이언트 접속 해제")
        list.remove(session)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val payload = message.payload
        println("payload $payload")

        list.forEach {
            it.sendMessage(message)
        }
    }

    override fun handleBinaryMessage(session: WebSocketSession, message: BinaryMessage) {
    }
}