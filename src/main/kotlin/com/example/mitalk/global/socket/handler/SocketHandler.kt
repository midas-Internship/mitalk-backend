package com.example.mitalk.global.socket.handler

import com.example.mitalk.domain.counsellor.persistence.CounsellorRepository
import com.example.mitalk.global.redis.util.CustomerQueueRedisUtils
import com.example.mitalk.global.socket.message.ChatMessage
import com.example.mitalk.global.socket.message.EnterQueueSuccessMessage
import com.example.mitalk.global.socket.message.QueueAlreadyFilledMessage
import com.example.mitalk.global.socket.util.SessionUtils
import com.example.mitalk.global.socket.util.MessageUtils
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.web.socket.BinaryMessage
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.UUID

@Component
class SocketHandler(
    private val mapper: ObjectMapper,
    private val messageUtils: MessageUtils,
    private val customerQueueRedisUtils: CustomerQueueRedisUtils,
    private val counsellorRepository: CounsellorRepository,
    private val sessionUtils: SessionUtils
) : TextWebSocketHandler() {

    //session connection 감지-------------------------------------------------------------------------------------------
    override fun afterConnectionEstablished(session: WebSocketSession) {
        //TODO attribute 검사
        val role = "customer"

        //고객인 경우 -> 대기열 입력
        if (role == "customer") {
            customerConnectEvent(session)
        } else if (role == "counsellor") {
            //TODO 식별키 가져오기
            val counsellorId = UUID.randomUUID()
            counsellorConnectionEvent(session, counsellorId)
        }

    }

    private fun customerConnectEvent(session: WebSocketSession) {
        if (!customerQueueRedisUtils.isQueueFull()) {
            customerQueueRedisUtils.zAdd(sessionUtils.add(session))
            println("$session 큐 입력")

            println(" " + customerQueueRedisUtils.zRange(1, -1))
            messageUtils.sendSystemMessage(
                message = EnterQueueSuccessMessage(customerQueueRedisUtils.zRank(session.id)),
                session = session
            )
        } else {
            println("$session 큐 입력 실패")

            messageUtils.sendSystemMessage(
                message = QueueAlreadyFilledMessage(),
                session = session
            )
        }
    }

    private fun counsellorConnectionEvent(session: WebSocketSession, id: UUID) {
        val counsellor = counsellorRepository.findByIdOrNull(id) ?: TODO("throw NotFoundException")

        counsellor.sessionConnectEvent(sessionUtils.add(session))
    }


    //session close 감지-------------------------------------------------------------------------------------------
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val removeCount = customerQueueRedisUtils.zDelete(session.id)
        println("queue에서 session $removeCount 개가 정상적으로 제거되었습니다.")

        println("$session 클라이언트 접속 해제 + $status")
    }

    //text message 감지-------------------------------------------------------------------------------------------
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val payload = message.payload
        val chatMessage = mapper.readValue(payload, ChatMessage::class.java)
        val room = counsellorRepository.findByRoomId(chatMessage.roomId) ?: TODO("NotFoundException")

        messageUtils.sendChatMessage(chatMessage, sessionUtils.get(room.customerSession!!), sessionUtils.get(room.counsellorSession!!))
    }

    //binary message 감지-------------------------------------------------------------------------------------------
    override fun handleBinaryMessage(session: WebSocketSession, message: BinaryMessage) {
    }
}