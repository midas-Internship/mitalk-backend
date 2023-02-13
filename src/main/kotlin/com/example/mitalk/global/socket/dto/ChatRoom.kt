package com.example.mitalk.global.socket.dto

import com.example.mitalk.global.socket.ChatService
import org.springframework.web.socket.WebSocketSession

class ChatRoom(
    val roomId: String,
    val name: String,
    val customerSession: WebSocketSession,
    val managerSession: WebSocketSession
) {

//    fun handleActions(session: WebSocketSession, chatMessage: ChatMessage, chatService: ChatService) {
//        var chatMessage = chatMessage
//        if (chatMessage.type == MessageType.ENTER) {
//            sessions.add(session)
//            chatMessage = chatMessage.setMessage("연결되었습니다.")
//        }
//        sendMessage(chatMessage, chatService)
//    }
//
//    private fun <T> sendMessage(message: T, chatService: ChatService) {
//        sessions.parallelStream().forEach {
//                s -> chatService.sendMessage(message, s)
//        }
//    }

}