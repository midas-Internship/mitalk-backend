package com.example.mitalk.domain.email.service

import com.example.mitalk.domain.auth.domain.Role
import com.example.mitalk.domain.email.presentation.data.dto.EmailSentDto
import com.example.mitalk.domain.record.domain.entity.CounsellingType
import com.example.mitalk.domain.record.domain.entity.Record
import com.example.mitalk.domain.record.domain.repository.RecordRepository
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.messaging.MessagingException
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
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
        val type = top1Record.counsellingType.name
        var list = ""
        for (it in top1Record.messageRecords) {// 고객 이냐 상담원이냐 여부
            for (i in 0 until it.dataMap.size) {
                if (it.sender.name == Role.CUSTOMER.name) {
                    list += ("        <p style=\"text-align: left;\"><b>고객</b> : ${it.dataMap[i].message} ${it.dataMap[i].localDateTime}</p>\n")
                } else if (it.sender.name == Role.COUNSELLOR.name) {
                    list += ("        <p style=\"text-align: left;\"><b>상담사</b> : ${it.dataMap[i].message} ${it.dataMap[i].localDateTime}</p>\n")
                }
            }
        }
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Document</title>\n" +
                "</head>\n" +
                "<body style=\"background-color: rgb(231, 231, 231)\">\n" +
                "    <h2>상담기록 안내</h2><hr><br>\n" +
                "    <p>안녕하세요 MiTalk팀의 전승원 상담사 입니다.</p>\n" +
                "    <table border=\"1\" style=\"text-align: center; border-color: rgb(146, 145, 145); border-width: 3px; border-style: solid;\">\n" +
                "        <tr>\n" +
                "            <td style=\"background-color: rgb(83, 83, 83); color: rgb(223, 221, 221);\">상담 내용</td>\n" +
                "            <td>${type}</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"background-color: rgb(83, 83, 83);  color: rgb(223, 221, 221);\">상담 일시</td>\n" +
                "            <td>2023-02-20 19:55:06 ~ 2023-02-21 19:55:06</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"background-color: rgb(83, 83, 83);  color: rgb(223, 221, 221);\">채팅 내용</td>\n" +
                "            <td>\n" +
                                list
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</body>\n" +
                "</html>"

    }


    private fun getRecordTemplate2(): String {
        var text = "        <p style=\"text-align: left;\"><b>고객</b> : {메시지} {시간}</p>\n"
        var list = ""
        for (i in 0..3) {
            if(i == 1) {
                list += ("        <p style=\"text-align: right; color: rgb(98, 204, 204);\"><b>고객</b> : ${"안녕"} ${LocalDateTime.now()}</p>\n")
            }
            list += ("        <p style=\"text-align: left; color: rgb(98, 204, 204);\"><b>고객</b> : ${"안녕"} ${LocalDateTime.now()}</p>\n")
        }
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Document</title>\n" +
                "</head>\n" +
                "<body style=\"background-color: rgb(231, 231, 231)\">\n" +
                "    <h2>상담기록 안내</h2><hr><br>\n" +
                "    <p>안녕하세요 MiTalk팀의 전승원 상담사 입니다.</p>\n" +
                "    <table border=\"1\" style=\"text-align: center; border-color: rgb(146, 145, 145); border-width: 3px; border-style: solid;\">\n" +
                "        <tr>\n" +
                "            <td style=\"background-color: rgb(83, 83, 83); color: rgb(223, 221, 221);\">상담 내용</td>\n" +
                "            <td>버그 관련</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"background-color: rgb(83, 83, 83);  color: rgb(223, 221, 221);\">상담 일시</td>\n" +
                "            <td>2023-02-20 19:55:06 ~ 2023-02-21 19:55:06</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"background-color: rgb(83, 83, 83);  color: rgb(223, 221, 221);\">채팅 내용</td>\n" +
                "            <td>\n" +
                "                <p style=\"text-align: left; font-size: 15px;\"><b>고객</b> : {메시지} {시간}</p> <p style=\"text-align: right; font-size: 15px;\"><b>상담사</b> : {메시지} {시간}</p>\n" +
                "                <p style=\"text-align: left; font-size: 15px\"><b>고객</b> : {메시지} {시간}</p> <p style=\"text-align: right; font-size: 15px;\"><b>상담사</b> : {메시지} {시간}</p>\n" +
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</body>\n" +
                "</html>"
    }
}