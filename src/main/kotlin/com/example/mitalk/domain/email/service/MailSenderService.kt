package com.example.mitalk.domain.email.service

import com.example.mitalk.domain.auth.domain.Role
import com.example.mitalk.domain.email.presentation.data.dto.EmailSentDto
import com.example.mitalk.domain.email.presentation.data.dto.TemplateDataDto
import com.example.mitalk.domain.email.presentation.domain.DocumentAllowed
import com.example.mitalk.domain.email.presentation.domain.ImageAllowed
import com.example.mitalk.domain.email.presentation.domain.VideoAllowed
import com.example.mitalk.domain.record.domain.entity.CounsellingType
import com.example.mitalk.domain.record.domain.entity.CounsellingType.*
import com.example.mitalk.domain.record.domain.entity.MessageRecord
import com.example.mitalk.domain.record.domain.entity.Record
import com.example.mitalk.domain.record.domain.repository.RecordRepository
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.messaging.MessagingException
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.thymeleaf.spring5.SpringTemplateEngine
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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

    private fun getRecordTemplate(top1Record: Record): String {
        val counsellingQuestionType = getCounselingType(top1Record.counsellingType)
        val date = "${getDateFormat(top1Record.startAt)} ~ ${getDateFormat(top1Record.messageRecords.last().dataMap.last().localDateTime)}"
        var list = ""
        for (it in top1Record.messageRecords) {// 고객 이냐 상담원이냐 여부
            for (i in 0 until it.dataMap.size) {

                if (it.sender.name == Role.CUSTOMER.name) {

                    if (checkS3URL(it.dataMap[i].message)) { // 이미지나 파일 URL
                        val extensionName = extractExtensionName(it.dataMap[i].message) // 확장자명
                        list += insertFileByExtensionName(extensionName.uppercase(), it.dataMap[i])
                    } else {
                        list += ("        <p style=\"text-align: left;\"><b>고객</b> : ${it.dataMap[i].message} ${getDateFormat(it.dataMap[i].localDateTime)}</p>\n")
                    }
                } else if (it.sender.name == Role.COUNSELLOR.name) {

                    if (checkS3URL(it.dataMap[i].message)) {
                        val extensionName = extractExtensionName(it.dataMap[i].message) // 확장자명
                        list += insertFileByExtensionName(extensionName.uppercase(), it.dataMap[i])
                    } else {
                        list += ("        <p style=\"text-align: left;\"><b>상담사</b> : ${it.dataMap[i].message} ${getDateFormat(it.dataMap[i].localDateTime)}</p>\n")
                    }
                }
            }
        }
        return getTemplate(TemplateDataDto(counsellingQuestionType, date, list))
    }

    private fun extractExtensionName(fileName: String): String {
        val index = fileName.lastIndexOf(".")

        if (index > 0) {
            return fileName.substring(index + 1)
        }
        return ""
    }

    private fun insertFileByExtensionName(extensionName: String, messageData: MessageRecord.MessageData): String { //
        if(ImageAllowed.values().map { it.name }.contains(extensionName)) {
            return "<img src=${messageData.message.replace("\"", "")}>"
        } else if(VideoAllowed.values().map { it.name }.contains(extensionName) || "MPEG-2" == extensionName) {
            return "<a href=${messageData.message.replace("\"", "")}>영상파일</a>"
        } else if(DocumentAllowed.values().map { it.name }.contains(extensionName)) {
            return "<a href=${messageData.message.replace("\"", "")}>문서파일</a>"
        }
        return ""
    }


    private fun checkS3URL(url: String): Boolean = url.contains("https://mitalk-s3.s3.ap-northeast-2.amazonaws.com/")

    private fun getCounselingType(type: CounsellingType): String {
        return when(type) {
            FEATURE_QUESTION -> "기능 질문"
            FEEDBACK -> "제품 피드백"
            BUG -> "버그 제보"
            FEATURE_PROPOSAL -> "기능 제안"
            PURCHASE -> "제휴 문의"
            ETC -> "기타"
        }
    }

    private fun getDateFormat(time: LocalDateTime): String {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

    private fun getTemplate(templateDataDto: TemplateDataDto): String {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"ko\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <meta property=\"og:type\" content=\"website\">\n" +
                "    <title>Document</title>\n" +
                "</head>\n" +
                "<body style=\"background-color: rgb(231, 231, 231)\">\n" +
                "    <h2>상담기록 안내</h2><hr><br>\n" +
                "    <p>안녕하세요 MiTalk팀의 전승원 상담사 입니다.</p>\n" +
                "    <table border=\"1\" style=\"text-align: center; border-color: rgb(109, 109, 109); border-width: 3px; border-style: solid; border-collapse: collapse;\">\n" +
                "        <tr>\n" +
                "            <td style=\"background-color: rgb(83, 83, 83); color: rgb(223, 221, 221); border-style: none;\">상담 내용</td>\n" +
                "            <td>${templateDataDto.counsellingType}</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"background-color: rgb(83, 83, 83);  color: rgb(223, 221, 221);\">상담 일시</td>\n" +
                "            <td>${templateDataDto.date}</td>\n" +
                "        </tr>\n" +
                "        <tr>\n" +
                "            <td style=\"background-color: rgb(83, 83, 83);  color: rgb(223, 221, 221);\">채팅 내용</td>\n" +
                "            <td>\n" +
                            templateDataDto.chatList
                "            </td>\n" +
                "        </tr>\n" +
                "    </table>\n" +
                "</body>\n" +
                "</html>"
    }

}