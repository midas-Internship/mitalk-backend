package com.example.mitalk.domain.email.service

import com.example.mitalk.domain.auth.domain.Role
import com.example.mitalk.domain.email.presentation.data.dto.EmailSentDto
import com.example.mitalk.domain.email.presentation.data.dto.TemplateDataDto
import com.example.mitalk.domain.email.presentation.domain.DocumentAllowed
import com.example.mitalk.domain.email.presentation.domain.ImageAllowed
import com.example.mitalk.domain.email.presentation.domain.VideoAllowed
import com.example.mitalk.domain.record.domain.entity.CounsellingType
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
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
        println("1")
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
        println("2")
        for (it in top1Record.messageRecords) {// 고객 이냐 상담원이냐 여부
            for (i in 0 until it.dataMap.size) {
                println("3")
                if (it.sender.name == Role.CUSTOMER.name) {
                    println("4")
                    if (checkS3URL(it.dataMap[i].message)) { // 이미지나 파일 URL
                        println("5")
                        val extensionName = extractExtensionName(it.dataMap[i].message) // 확장자명
                        list += insertFileByExtensionName(extensionName.uppercase(), it.dataMap[i], "${top1Record.customerName} 고객님")
                    } else {
                        println("6")
                        list += ("<div class=\"chat-item-box\">\n" +
                                "    <p class=\"chat-item-name\">${top1Record.customerName} 고객님</p>\n" +
                                "    <div class=\"chat-item-content\">\n" +
                                "        <p class=\"chat-p\">${it.dataMap[i].message}</p>\n" +
                                "        <a class=\"chat-item-content-time\">${it.dataMap[i].localDateTime}</a>\n" +
                                "    </div>\n" +
                                "</div>")
                    }
                } else if (it.sender.name == Role.COUNSELLOR.name) {

                    if (checkS3URL(it.dataMap[i].message)) {
                        val extensionName = extractExtensionName(it.dataMap[i].message) // 확장자명
                        list += insertFileByExtensionName(extensionName.uppercase(), it.dataMap[i], "${top1Record.counsellorName} 상담사님")
                    } else {
                        list += ("<div class=\"chat-item-box right\">\n" +
                                "    <p class=\"chat-item-name\">${top1Record.counsellorName} 상담사님</p>\n" +
                                "    <div class=\"chat-item-content\">\n" +
                                "        <a class=\"chat-item-content-time\">${it.dataMap[i].message}</a>\n" +
                                "        <p class=\"chat-p\">${it.dataMap[i].message}</p>\n" +
                                "    </div>\n" +
                                "</div>")
                    }
                }
            }
        }
        println("7")
        return getTemplate(TemplateDataDto(counsellingQuestionType, date, list))
    }

    private fun extractExtensionName(fileName: String): String {
        val index = fileName.lastIndexOf(".")

        if (index > 0) {
            return fileName.substring(index + 1)
        }
        return ""
    }

    private fun insertFileByExtensionName(extensionName: String, messageData: MessageRecord.MessageData, name: String): String { //
        var direction = ""
        if(name.contains("고객님")) {
           direction = ""
        }
        else if(name.contains("상담사님")) {
            direction = "right"
        }

        if (ImageAllowed.values().map { it.name }.contains(extensionName)) {
            return "<div class=\"chat-item-box ${direction}\">\n" +
                    "    <p class=\"chat-item-name\">$name</p>\n" +
                    "    <div class=\"chat-item-content\">\n" +
                    "        <a class=\"chat-item-content-time\">${messageData.localDateTime}</a>\n" +
                    "        <img class=\"chat-img\"\n" +
                    "            src=${messageData.message.replace("\"", "")} />\n" +
                    "    </div>\n" +
                    "</div>"
        } else if (VideoAllowed.values().map { it.name }.contains(extensionName) || "MPEG-2" == extensionName) {
            return "<div class=\"chat-item-box right\">\n" +
                    "    <p class=\"chat-item-name\">$name</p>\n" +
                    "    <div class=\"chat-item-content\">\n" +
                    "        <a class=\"chat-item-content-time\">am 2:40</a>\n" +
                    "        <a class=\"chat-download\" href=${messageData.message.replace("\"", "")}>영상 다운로드\n" +
                    "            <img src=https://cdn.discordapp.com/attachments/814021755588182067/1077818737597632543/Vector.png />\n" +
                    "        </a>\n" +
                    "    </div>\n" +
                    "</div>"
        } else if (DocumentAllowed.values().map { it.name }.contains(extensionName)) {
            return "<div class=\"chat-item-box right\">\n" +
                    "    <p class=\"chat-item-name\">$name</p>\n" +
                    "    <div class=\"chat-item-content\">\n" +
                    "        <a class=\"chat-item-content-time\">am 2:40</a>\n" +
                    "        <a class=\"chat-download\" href=${messageData.message.replace("\"", "")}>파일 다운로드\n" +
                    "            <img src=https://cdn.discordapp.com/attachments/814021755588182067/1077818737597632543/Vector.png />\n" +
                    "        </a>\n" +
                    "    </div>\n" +
                    "</div>"
        }
        return ""
    }


    private fun checkS3URL(url: String): Boolean = url.contains("http://d10pkqgki4fe73.cloudfront.net/")

    private fun getCounselingType(type: CounsellingType): String {
        return when (type) {
            CounsellingType.FEATURE_QUESTION -> "기능 질문"
            CounsellingType.FEEDBACK -> "제품 피드백"
            CounsellingType.BUG -> "버그 제보"
            CounsellingType.FEATURE_PROPOSAL -> "기능 제안"
            CounsellingType.PURCHASE -> "제휴 문의"
            CounsellingType.ETC -> "기타"
        }
    }

    private fun getDateFormat(time: LocalDateTime): String {
        return time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
    }

    private fun getTemplate(templateDataDto: TemplateDataDto): String {
        return "<!DOCTYPE html>\n" +
                "<html lang=\"ko\">\n" +
                "\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\" />\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\" />\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                "    <title>Document</title>\n" +
                "    <style>\n" +
                "        * {\n" +
                "            box-sizing: border-box;\n" +
                "            margin: 0;\n" +
                "            padding: 0;\n" +
                "        }\n" +
                "\n" +
                "        h1,\n" +
                "        p {\n" +
                "            color: #262626;\n" +
                "        }\n" +
                "\n" +
                "        body {\n" +
                "            display: flex;\n" +
                "            justify-content: center;\n" +
                "            align-items: center;\n" +
                "        }\n" +
                "\n" +
                "        #Wraaper {\n" +
                "            width: 1440px;\n" +
                "            height: 1768px;\n" +
                "            background: #f7f8fa;\n" +
                "            padding: 0 364px;\n" +
                "        }\n" +
                "\n" +
                "        header {\n" +
                "            width: 100%;\n" +
                "            height: 204px;\n" +
                "            display: flex;\n" +
                "            align-items: center;\n" +
                "        }\n" +
                "\n" +
                "        #title {\n" +
                "            width: 100%;\n" +
                "            height: 108px;\n" +
                "            display: flex;\n" +
                "            flex-direction: column;\n" +
                "            justify-content: space-between;\n" +
                "        }\n" +
                "\n" +
                "        #title>h1 {\n" +
                "            font-weight: 700;\n" +
                "            font-size: 35px;\n" +
                "            color: #262626;\n" +
                "        }\n" +
                "\n" +
                "        #title>P {\n" +
                "            font-weight: 500;\n" +
                "            font-size: 20px;\n" +
                "            color: #2f76ff;\n" +
                "        }\n" +
                "\n" +
                "        .explanation-box {\n" +
                "            width: 100%;\n" +
                "            margin-top: 20px;\n" +
                "        }\n" +
                "\n" +
                "        .explanation-box>p {\n" +
                "            font-weight: 700;\n" +
                "            font-size: 22px;\n" +
                "        }\n" +
                "\n" +
                "        .explanation-box>div {\n" +
                "            width: 712px;\n" +
                "            height: 76px;\n" +
                "            background: #ffffff;\n" +
                "            border-radius: 5px;\n" +
                "            margin-top: 5px;\n" +
                "            display: flex;\n" +
                "            justify-content: center;\n" +
                "            flex-direction: column;\n" +
                "            padding: 24px 47px;\n" +
                "        }\n" +
                "\n" +
                "        .explanation-box li {\n" +
                "            font-size: 22px;\n" +
                "            line-height: 50px;\n" +
                "        }\n" +
                "\n" +
                "        #chat-box {\n" +
                "            width: 100%;\n" +
                "            margin-top: 41px;\n" +
                "            word-break:break-all;\n" +
                "        }\n" +
                "\n" +
                "        #chat-box>p {\n" +
                "            font-weight: 700;\n" +
                "            font-size: 22px;\n" +
                "        }\n" +
                "\n" +
                "        #chat-box>div {\n" +
                "            margin-top: 20px;\n" +
                "            padding: 0 20px;\n" +
                "            width: 100%;\n" +
                "            height: 638px;\n" +
                "            background: #ffffff;\n" +
                "            border-radius: 5px;\n" +
                "            display: flex;\n" +
                "            flex-direction: column;\n" +
                "            overflow: scroll;\n" +
                "            overflow-y: hidden;\n" +
                "        }\n" +
                "\n" +
                "        .chat-item-box {\n" +
                "            width: 100%;\n" +
                "            margin-top: 15px;\n" +
                "        }\n" +
                "\n" +
                "        .chat-item-name {\n" +
                "            width: 100%;\n" +
                "            font-weight: 700;\n" +
                "            font-size: 16px;\n" +
                "        }\n" +
                "\n" +
                "        .chat-item-content {\n" +
                "            margin-top: 14px;\n" +
                "            width: 100%;\n" +
                "            display: flex;\n" +
                "            justify-content: flex-start;\n" +
                "        }\n" +
                "\n" +
                "        .chat-item-content-time {\n" +
                "            display: flex;\n" +
                "            flex-direction: column;\n" +
                "            justify-content: end;\n" +
                "            left: 100%;\n" +
                "            bottom: 0;\n" +
                "            font-weight: 350;\n" +
                "            font-size: 15px;\n" +
                "            margin: 0 5px;\n" +
                "        }\n" +
                "\n" +
                "        .right>p {\n" +
                "            text-align: right;\n" +
                "        }\n" +
                "\n" +
                "        .right>div {\n" +
                "            justify-content: flex-end;\n" +
                "        }\n" +
                "\n" +
                "        .chat-p {\n" +
                "            font-weight: 400;\n" +
                "            font-size: 18px;\n" +
                "            padding: 10px;\n" +
                "            background: #f2f2f2;\n" +
                "            border-radius: 10px;\n" +
                "        }\n" +
                "\n" +
                "        .chat-img {\n" +
                "            width: 173px;\n" +
                "            height: 139px;\n" +
                "            object-fit: cover;\n" +
                "            border-radius: 10px;\n" +
                "        }\n" +
                "\n" +
                "        .chat-download {\n" +
                "            cursor: pointer;\n" +
                "            padding: 10px;\n" +
                "            border-radius: 10px;\n" +
                "            color: #3b7eff;\n" +
                "            font-weight: 500;\n" +
                "            font-size: 18px;\n" +
                "            background: #f2f2f2;\n" +
                "            display: flex;\n" +
                "            gap: 10px;\n" +
                "            align-items: center;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                "    <div id=\"Wraaper\">\n" +
                "        <header>\n" +
                "            <img id=\"logo\" src=\"./Group_12.png\" />\n" +
                "        </header>\n" +
                "        <section>\n" +
                "            <div id=\"title\">\n" +
                "                <h1>상담 기록 안내</h1>\n" +
                "                <p>안녕하세요 Mitalk 관계자 전승원입니다.</p>\n" +
                "            </div>\n" +
                "            <div class=\"explanation-box\">\n" +
                "                <p>상담 내용</p>\n" +
                "                <div>\n" +
                "                    <ul>\n" +
                "                        <li>${templateDataDto.counsellingType}</li>\n" +
                "                    </ul>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "            <div class=\"explanation-box\">\n" +
                "                <p>상담 일시</p>\n" +
                "                <div>\n" +
                "                    <ul>\n" +
                "                        <li>${templateDataDto.date}</li>\n" +
                "                    </ul>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "            <div id=\"chat-box\">\n" +
                "                <p>채팅 내용</p>\n" +
                "                <div>\n" +
                                    templateDataDto.chatList
                "                </div>\n" +
                "            </div>\n" +
                "        </section>\n" +
                "    </div>\n" +
                "</body>\n" +
                "\n" +
                "</html>"
    }

}