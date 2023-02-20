package com.example.mitalk.domain.email.service

import com.example.mitalk.domain.email.presentation.data.dto.EmailSentDto
import com.example.mitalk.domain.record.domain.entity.MessageRecord
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

    @Async
    @Transactional(rollbackFor = [Exception::class])
    fun execute2(email: String) {
        sendAuthEmail2(email)
    }

    private fun sendAuthEmail(email: String, customerId: UUID) {
        val top1Record = recordRepository.findTop1ByCustomerIdOrderByStartAtAsc(customerId)
        val subject = "Mitalk 상담 기록 데이터"
        val recordTemplate = getRecordTemplate(top1Record.messageRecords)
        try {
            val mimeMessage: MimeMessage = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(mimeMessage, true, "utf-8")
            helper.setTo(email)
            helper.setSubject(subject)
            helper.setText(recordTemplate, true)
            mailSender.send(mimeMessage)
        } catch (e: MessagingException) {
            throw RuntimeException("메일 발송에 실패했습니다")
        }
    }




    private fun sendAuthEmail2(email: String) {
        val subject = "Mitalk 상담 기록 데이터"
        val recordTemplate = getRecordTemplate2()
        try {
            val mimeMessage: MimeMessage = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(mimeMessage, true, "utf-8")
            helper.setTo(email)
            helper.setSubject(subject)
            helper.setText(recordTemplate, true)
            mailSender.send(mimeMessage)
        } catch (e: MessagingException) {
            throw RuntimeException("메일 발송에 실패했습니다")
        }
    }

    private fun getRecordTemplate(messageRecordList: List<MessageRecord>): String {
        var text = "        <p style=\"text-align: left;\"><b>고객</b> : {메시지} {시간}</p>\n" +
        messageRecordList
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Document</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "        <p style=\"text-align: left;\"><b>고객</b> : {메시지} {시간}</p>\n" +
                text
                "        <p style=\"text-align: left;\"><b>상담사</b> : {메시지} {시간}</p>\n" +
                "</body>\n" +
                "</html>"
    }


    private fun getRecordTemplate2(): String {
        var text = "        <p style=\"text-align: left;\"><b>고객</b> : {메시지} {시간}</p>\n"
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Document</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "        <p style=\"text-align: left;\"><b>고객</b> : {메시지} {시간}</p>\n" +
                text +
        "        <p style=\"text-align: left;\"><b>상담사</b> : {메시지} {시간}</p>\n" +
                "</body>\n" +
                "</html>"
    }
}