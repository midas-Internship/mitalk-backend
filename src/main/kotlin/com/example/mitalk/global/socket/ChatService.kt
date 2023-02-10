package com.example.mitalk.global.socket

import com.example.mitalk.global.socket.dto.ChatRoom
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.UUID
import javax.annotation.PostConstruct

@Service
class ChatService(
    val mapper: ObjectMapper
) {
    lateinit var chatRooms: MutableMap<String, ChatRoom>

    @PostConstruct
    private fun init() {
        chatRooms = mutableMapOf()
    }

    fun findAllRoom() = chatRooms.values.toList()

    fun findRoomById(roomId: String) = chatRooms[roomId]

    fun createRoom(name: String): ChatRoom {
        val roomId: String = UUID.randomUUID().toString()
        val chatRoom = ChatRoom(roomId = roomId, name = name)
        chatRooms[roomId] = chatRoom
        return chatRoom
    }

    fun <T> sendMessage(message: T, session: WebSocketSession) {
        session.sendMessage(TextMessage(mapper.writeValueAsString(message)))
    }
}