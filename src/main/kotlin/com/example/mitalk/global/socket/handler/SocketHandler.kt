package com.example.mitalk.global.socket.handler

import com.example.mitalk.domain.auth.domain.Role
import com.example.mitalk.domain.counsellor.domain.entity.Counsellor
import com.example.mitalk.domain.counsellor.domain.entity.CounsellorStatus
import com.example.mitalk.domain.counsellor.domain.repository.CounsellorRepository
import com.example.mitalk.domain.customer.domain.entity.Customer
import com.example.mitalk.domain.customer.domain.entity.CustomerInfo
import com.example.mitalk.domain.customer.domain.entity.CustomerQueue
import com.example.mitalk.domain.customer.domain.repository.CustomerInfoRepository
import com.example.mitalk.domain.customer.domain.repository.CustomerRepository
import com.example.mitalk.domain.email.presentation.data.dto.EmailSentDto
import com.example.mitalk.domain.email.service.MailSenderService
import com.example.mitalk.domain.record.domain.entity.CounsellingType
import com.example.mitalk.domain.record.domain.entity.MessageRecord
import com.example.mitalk.domain.record.domain.repository.RecordRepository
import com.example.mitalk.global.security.jwt.JwtTokenProvider
import com.example.mitalk.global.socket.message.*
import com.example.mitalk.global.socket.util.MessageUtils
import com.example.mitalk.global.socket.util.SessionUtils
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.time.LocalDateTime
import java.util.*

@Component
class SocketHandler(
    private val mapper: ObjectMapper,
    private val messageUtils: MessageUtils,
    private val customerQueue: CustomerQueue,
    private val counsellorRepository: CounsellorRepository,
    private val sessionUtils: SessionUtils,
    private val tokenProvider: JwtTokenProvider,
    private val customerInfoRepository: CustomerInfoRepository,
    private val recordRepository: RecordRepository,
    private val customerRepository: CustomerRepository,
    private val mailSenderService: MailSenderService
) : TextWebSocketHandler() {

    @Value("\${cloud.aws.s3.url}")
    lateinit var fileIdentification: String

    //session connection 감지-------------------------------------------------------------------------------------------
    @Transactional
    override fun afterConnectionEstablished(session: WebSocketSession) {
        //토큰 검증
        val token = session.handshakeHeaders["Authorization"] ?: TODO("Authorization not found exception")
        println("token : " + token[0])
        val resolvedToken = tokenProvider.parseToken(token[0]) ?: TODO("ERROR")
        val claims = tokenProvider.socketAuthentication(resolvedToken)
        val role = claims.get(JwtTokenProvider.AUTHORITY, String::class.java)

        val id = claims.subject

        val roomId = session.handshakeHeaders["RoomId"]!!

        //고객인 경우 -> 대기열 세션 입력
        if (Role.CUSTOMER.name == role && roomId[0] == "null") {
            val type = session.handshakeHeaders["ChatType"] ?: TODO("Type not found exception")
            customerConnectEvent(session, customerRepository.findByEmail(id)!!.id, CounsellingType.valueOf(type[0]))
            //상담사인 경우 -> MONGO 세션 입력
        } else if (Role.COUNSELLOR.name == role) {
            //TODO 식별키 가져오기
            counsellorConnectionEvent(session, UUID.fromString(id))
        } else if (roomId[0] != "null"){
            val roomId = UUID.fromString(roomId[0])
            val counsellor = counsellorRepository.findByRoomId(roomId) ?: TODO("Invalid room Id")
            counsellorRepository.save(counsellor.reconnect(sessionUtils.add(session)))
            val customerInfo = customerInfoRepository.findByIdOrNull(customerRepository.findByEmail(id)!!.id) ?: TODO("CustomerInfo Not Found")
            customerInfoRepository.save(
                CustomerInfo(customerInfo.customerId, session.id, customerInfo.type)
            )

            messageUtils.sendSystemMessage(CounsellingStartMessage(roomId, counsellor.name), session)
        }

    }

    private fun customerConnectEvent(session: WebSocketSession, id: UUID, type: CounsellingType) {
        if (!customerQueue.isQueueFull()) {
            customerQueue.zAdd(sessionUtils.add(session))
            println("$session 큐 입력")

            messageUtils.sendSystemMessage(
                message = EnterQueueSuccessMessage(customerQueue.zRank(session.id)),
                session = session
            )
            customerInfoRepository.save(
                CustomerInfo(customerId = id, customerSessionId = session.id, type)
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
    /*
     * 수정
     * counsellor 일때
     * 1. roomBurstMessage 전송
     * 2. 사용자한테 mail 발송
     * 3. customerInfo 제거
     * 4. sessionFactory 에서 둘다 제거
     * 5. roomCloseEvent
     */
    @Transactional
    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {

        val customerInfo = customerInfoRepository.findByCustomerSessionId(session.id)

        if (customerInfo == null) {

            val counsellor = counsellorRepository.findByCounsellorSession(session.id)

            if (counsellor != null) {

                messageUtils.sendSystemMessage(RoomBurstEventMessage(), sessionUtils.get(counsellor.customerSession ?: TODO("Customer SessionId NotFound")))

                val customerInfo = customerInfoRepository.findByCustomerSessionId(counsellor.customerSession) ?: TODO("CustomerInfo NotFound")
                val customer = customerRepository.findByIdOrNull(customerInfo.customerId) ?: TODO("Customer NotFound")

                mailSenderService.execute(EmailSentDto(customer.email, customerInfo.customerId))

                customerInfoRepository.delete(customerInfo)

                customerRepository.save(Customer(
                    id = customer.id, name = customer.name, email = customer.email, picture = customer.picture, needReview = counsellor.id
                ))

                sessionUtils.remove(counsellor.customerSession)

                counsellorRepository.save(Counsellor(
                        counsellor.id, roomId = null,
                        counsellor.name, counsellorSession = null,
                        customerSession = null, counsellor.todayCounsellingCount,
                        CounsellorStatus.OFFLINE
                ))
//                counsellorRepository.save(counsellor.roomCloseEvent())

                println("${counsellor.roomId} 룸 폭파")
            }
        }

        sessionUtils.remove(session.id)

        println("${session.id} 클라이언트 접속 해제")
    }

    //text message 감지-------------------------------------------------------------------------------------------
    @Transactional
    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val payload = message.payload
        println("데이터 값 : ${message.payload}")
        var chatMessage = mapper.readValue(payload, ChatMessage::class.java)
        val counsellor = counsellorRepository.findByRoomId(chatMessage.roomId) ?: TODO("NotFoundException")
        val record = recordRepository.findByIdOrNull(counsellor.roomId) ?: TODO("ERROR")

        if (ChatMessage.ChatMessageType.SEND == chatMessage.chatMessageType) {
            println("send 메세지도착 $chatMessage")
            val newMessageId = UUID.randomUUID()
            chatMessage = ChatMessage(chatMessage.roomId, newMessageId, chatMessage.role, chatMessage.chatMessageType, chatMessage.message)
            recordRepository.save(
                record.add(
                    MessageRecord(
                        messageId = newMessageId,
                        sender = chatMessage.role,
                        isFile = chatMessage.message!!.contains(fileIdentification),
                        isDeleted = false,
                        isUpdated = false,
                        dataMap = mutableListOf(MessageRecord.MessageData(chatMessage.message!!, LocalDateTime.now()))
                    )
                )
            )
        } else if (ChatMessage.ChatMessageType.UPDATE == chatMessage.chatMessageType){
            val messageRecord = record.findMessageRecordById(chatMessage.messageId?: TODO("Message Id Notfound")) ?: TODO("Message Not found")
            recordRepository.save(
                record.updateMessageRecord(
                    messageRecord.updateData(
                        chatMessage.message!!
                    )
                )
            )

        } else if (ChatMessage.ChatMessageType.DELETE == chatMessage.chatMessageType) {
            val messageRecord = record.findMessageRecordById(chatMessage.messageId!!) ?: TODO("Message Not found")
            recordRepository.save(
                record.updateMessageRecord(
                    messageRecord.deleteData()
                )
            )
        }


        messageUtils.sendChatMessage(
            chatMessage,
            sessionUtils.get(counsellor.customerSession!!),
            sessionUtils.get(counsellor.counsellorSession!!)
        )

    }
}