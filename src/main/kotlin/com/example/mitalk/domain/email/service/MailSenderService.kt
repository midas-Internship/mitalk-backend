package com.example.mitalk.domain.email.service

import com.example.mitalk.domain.email.presentation.data.dto.EmailSentDto
import com.example.mitalk.domain.record.domain.repository.RecordRepository
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.messaging.MessagingException
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import javax.mail.internet.MimeMessage


@Service
@EnableAsync
class MailSenderService(
        private val mailSender: JavaMailSender,
        private val recordRepository: RecordRepository
) {
    @Async
    @Transactional(rollbackFor = [Exception::class])
    fun execute(emailSentDto: EmailSentDto) {
        sendAuthEmail(emailSentDto.email, emailSentDto.customerSessionId)
    }

    private fun sendAuthEmail(email: String, customerId: UUID) {
        val top1Record = recordRepository.findTop1ByCustomerIdOrderByStartAtAsc(customerId)
        val subject = "Mitalk 상담 기록 데이터"
        val text = top1Record.messageRecords.toString()
        try {
            val mimeMessage: MimeMessage = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(mimeMessage, true, "utf-8")
            helper.setTo(email)
            helper.setSubject(subject)
            helper.setText(text, true)
            mailSender.send(mimeMessage)
        } catch (e: MessagingException) {
            throw RuntimeException("메일 발송에 실패했습니다")
        }
    }
}