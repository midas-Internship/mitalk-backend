package com.example.mitalk.domain.email.service

import com.example.mitalk.domain.email.presentation.data.dto.EmailSentDto
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.messaging.MessagingException
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.mail.internet.MimeMessage


@Service
@EnableAsync
class MailSenderService(
        private val mailSender: JavaMailSender
) {
    @Async
    @Transactional(rollbackFor = [Exception::class])
    fun execute(emailSentDto: EmailSentDto) {
        sendAuthEmail(emailSentDto.email)
    }

    private fun sendAuthEmail(email: String) {
        val subject = "TOOK 인증번호"
        val text = "회원 가입을 위한 인증번호는 입니다. <br />"
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