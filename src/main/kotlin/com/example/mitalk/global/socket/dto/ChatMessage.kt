package com.example.mitalk.global.socket.dto

data class ChatMessage(
    val type: MessageType,
    val roomId: String,
    val sender: String,
    val message: String?
) {
    fun setMessage(newMessage: String) = ChatMessage(type, roomId, sender, newMessage)
}

enum class MessageType {
    ENTER,
    TALK,
    SYSTEM
}