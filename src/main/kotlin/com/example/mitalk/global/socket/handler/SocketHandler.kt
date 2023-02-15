package com.example.mitalk.global.socket.handler

import com.example.mitalk.domain.auth.domain.Role
import com.example.mitalk.domain.counsellor.domain.repository.CounsellorRepository
import com.example.mitalk.domain.customer.domain.entity.CustomerIdHash
import com.example.mitalk.domain.customer.domain.entity.CustomerQueue
import com.example.mitalk.domain.customer.domain.repository.CustomerIdHashRepository
import com.example.mitalk.domain.customer.domain.repository.CustomerRepository
import com.example.mitalk.domain.record.domain.entity.MessageRecord
import com.example.mitalk.domain.record.domain.repository.RecordRepository
import com.example.mitalk.global.security.jwt.JwtTokenProvider
import com.example.mitalk.global.socket.message.ChatMessage
import com.example.mitalk.global.socket.message.EnterQueueSuccessMessage
import com.example.mitalk.global.socket.message.QueueAlreadyFilledMessage
import com.example.mitalk.global.socket.message.RoomBurstEventMessage
import com.example.mitalk.global.socket.util.SessionUtils
import com.example.mitalk.global.socket.util.MessageUtils
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.web.socket.BinaryMessage
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.time.LocalDateTime
import java.util.UUID

@Component
class SocketHandler(
    private val mapper: ObjectMapper,
    private val messageUtils: MessageUtils,
    private val customerQueue: CustomerQueue,
    private val counsellorRepository: CounsellorRepository,
    private val sessionUtils: SessionUtils,
    private val tokenProvider: JwtTokenProvider,
    private val customerIdHashRepository: CustomerIdHashRepository,
    private val recordRepository: RecordRepository,
    private val customerRepository: CustomerRepository
) : TextWebSocketHandler() {

    @Value("\${cloud.aws.s3.url}")
    lateinit var fileIdentification: String

    //session connection 감지-------------------------------------------------------------------------------------------
    override fun afterConnectionEstablished(session: WebSocketSession) {
        val token = session.handshakeHeaders["Authorization"] ?: TODO("Authorization not found exception")
        val resolvedToken = tokenProvider.parseToken(token[0]) ?: TODO("ERROR")
        val claims = tokenProvider.socketAuthentication(resolvedToken)
        val role = claims.get(JwtTokenProvider.AUTHORITY, String::class.java)

        val id = claims.subject

        //고객인 경우 -> 대기열 세션 입력
        if (Role.CUSTOMER.name == role) {
            customerConnectEvent(session, customerRepository.findByEmail(id)!!.id)
            //상담사인 경우 -> MONGO 세션 입력
        } else if (Role.COUNSELLOR.name == role) {
            //TODO 식별키 가져오기
            counsellorConnectionEvent(session, UUID.randomUUID())
        }

    }

    private fun customerConnectEvent(session: WebSocketSession, id: UUID) {
        if (!customerQueue.isQueueFull()) {
            customerQueue.zAdd(sessionUtils.add(session))
            println("$session 큐 입력")

            messageUtils.sendSystemMessage(
                message = EnterQueueSuccessMessage(customerQueue.zRank(session.id)),
                session = session
            )
            customerIdHashRepository.save(
                CustomerIdHash(customerId = id, customerSessionId = session.id)
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

        counsellorRepository.save(
            counsellor.sessionConnectEvent(sessionUtils.add(session))
        )
    }


    //session close 감지-------------------------------------------------------------------------------------------
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val removeCount = customerQueue.zDelete(session.id)
        if (removeCount == 0L) { //상담원이 나갔을때
            val counsellor = counsellorRepository.findByCounsellorSession(session.id)!!
            counsellor.customerSession ?: return

            counsellorRepository.save(counsellor.roomCloseEvent())
            messageUtils.sendSystemMessage(RoomBurstEventMessage(), sessionUtils.get(counsellor.customerSession))
        } else { //사용자가 나갔을때
            val counsellor = counsellorRepository.findByCustomerSession(session.id) ?: return
            //TODO ses 메일로 발송
            counsellorRepository.save(counsellor.roomCloseEvent())
            messageUtils.sendSystemMessage(RoomBurstEventMessage(), sessionUtils.get(counsellor.counsellorSession!!))
            customerIdHashRepository.deleteByCustomerSessionId(session.id)
            println("sessionFactory에서 session $removeCount 개가 정상적으로 제거되었습니다.")
        }

        println("$session 클라이언트 접속 해제 + $status")
    }

    //text message 감지-------------------------------------------------------------------------------------------
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val payload = message.payload
        val chatMessage = mapper.readValue(payload, ChatMessage::class.java)
        val counsellor = counsellorRepository.findByRoomId(chatMessage.roomId) ?: TODO("NotFoundException")

        messageUtils.sendChatMessage(
            chatMessage,
            sessionUtils.get(counsellor.customerSession!!),
            sessionUtils.get(counsellor.counsellorSession!!)
        )

        val record = recordRepository.findByIdOrNull(counsellor.roomId) ?: TODO("ERROR")

        recordRepository.save(
            record.add(
                MessageRecord(
                    sender = chatMessage.role,
                    isFile = chatMessage.message.contains(fileIdentification),
                    isDeleted = false,
                    isUpdated = false,
                    dataMap = linkedMapOf(chatMessage.message to LocalDateTime.now())
                )
            )
        )


    }

    //binary message 감지-------------------------------------------------------------------------------------------
    override fun handleBinaryMessage(session: WebSocketSession, message: BinaryMessage) {
    }
}