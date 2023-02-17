package com.example.mitalk.domain.email.presentation

import com.example.mitalk.domain.email.presentation.data.dto.EmailSentDto
import com.example.mitalk.domain.email.service.MailSenderService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class MailController(
        private val mailSenderService: MailSenderService
) {
    @PostMapping("/test")
    fun sendMail(@RequestBody email: EmailSentDto) {
        mailSenderService.execute(email)
    }
}
