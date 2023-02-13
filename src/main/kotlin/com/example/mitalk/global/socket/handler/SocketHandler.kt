package com.example.mitalk.global.socket.handler

import com.example.mitalk.global.redis.util.RedisUtils
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
    val redisUtils: RedisUtils
) : TextWebSocketHandler() {

    //session connection 감지
    override fun afterConnectionEstablished(session: WebSocketSession) {
        //TODO("attribute 검사")

        //고객인 경우 -> 대기열 입력
        if (!redisUtils.isQueueFull()) {
            redisUtils.zAdd(session)
            println("$session 큐 입력")

            //TODO("안드로이드 한테 큐접속 했다 보내기 + 현재 rank")
            //session.sendMessage()
        } else {
            println("$session 큐 입력 실패")
            //TODO("안드한테 실패했다고 보내기")
        }

        //

    }

    //session 종료 감지
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val removeCount = redisUtils.zDelete(session)
        println("queue에서 session $removeCount 개가 정상적으로 제거되었습니다.")

        println("$session 클라이언트 접속 해제 + $status")

        //TODO("안드한테 접속해제됬다고 보내기")

    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val payload = message.payload
        println("payload = $payload")
        val chatMessage = mapper.readValue(payload, ChatMessage::class.java)
        val room = chatService.findRoomById(chatMessage.roomId)
        if(room != null) {
//            room.handleActions(session, chatMessage, chatService)
        } else println("room was not exist")
    }

    override fun handleBinaryMessage(session: WebSocketSession, message: BinaryMessage) {
    }
}