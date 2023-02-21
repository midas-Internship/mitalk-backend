package com.example.mitalk.domain.email.service

import com.example.mitalk.domain.auth.domain.Role
import com.example.mitalk.domain.email.presentation.data.dto.EmailSentDto
import com.example.mitalk.domain.record.domain.entity.Record
import com.example.mitalk.domain.record.domain.repository.RecordRepository
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.messaging.MessagingException
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.SpringTemplateEngine
import java.time.LocalDateTime
import java.util.*
import javax.mail.internet.MimeMessage


@Service
@EnableAsync
class MailSenderService(private val mailSender: JavaMailSender, private val recordRepository: RecordRepository, private val templateEngine: SpringTemplateEngine) {
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
        val top1Record = recordRepository.findTop1ByCustomerIdOrderByStartAtDesc(customerId)

        val subject = "Mitalk 상담 기록 데이터"
        val recordTemplate = getRecordTemplate(top1Record)
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

    private fun getRecordTemplate(top1Record: Record): String {
        val type = top1Record.counsellingType
        val date = "${top1Record.startAt} ~ ${top1Record.messageRecords.last().dataMap.last().localDateTime}"
        var list = ""
        for (it in top1Record.messageRecords) {// 고객 이냐 상담원이냐 여부
            for (i in 0 until it.dataMap.size) {

                if (it.sender.name == Role.CUSTOMER.name) {

                    if (checkS3URL(it.dataMap[i].message)) { // 이미지나 파일 URL
                        val extensionName = extractExtensionName(it.dataMap[i].message)
                    } else {
                        list += ("        <p style=\"text-align: left;\"><b>고객</b> : ${it.dataMap[i].message} ${it.dataMap[i].localDateTime}</p>\n")
                    }
                } else if (it.sender.name == Role.COUNSELLOR.name) {

                    if (checkS3URL(it.dataMap[i].message)) {

                    } else {
                        list += ("        <p style=\"text-align: left;\"><b>상담사</b> : ${it.dataMap[i].message} ${it.dataMap[i].localDateTime}</p>\n")
                    }
                }
            }
        }
        return ""
    }


    private fun getRecordTemplate2(): String {
        val context = Context()
        val html: String = templateEngine.process("test", context)
        var text = "        <p style=\"text-align: left;\"><b>고객</b> : {메시지} {시간}</p>\n"
        var list = ""
        for (i in 0..3) {
            if (i == 1) {
                list += ("        <p style=\"text-align: right; color: rgb(98, 204, 204);\"><b>고객</b> : ${"안녕"} ${LocalDateTime.now()}</p>\n")
            }
            list += ("        <p style=\"text-align: left; color: rgb(98, 204, 204);\"><b>고객</b> : ${"안녕"} ${LocalDateTime.now()}</p>\n")
        }
        return html
    }

    private fun extractExtensionName(fileName: String): String {
        val index = fileName.lastIndexOf(".")

        if (index > 0) {
            return fileName.substring(index + 1)
        }
        return ""
    }

    private fun checkS3URL(url: String): Boolean = url.contains("https://mitalk-s3.s3.ap-northeast-2.amazonaws.com/")
}