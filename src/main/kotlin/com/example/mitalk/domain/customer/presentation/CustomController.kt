package com.example.mitalk.domain.customer.presentation

import com.example.mitalk.domain.customer.domain.entity.CustomerSession
import com.example.mitalk.domain.customer.domain.repository.CustomerSessionRepository
import com.example.mitalk.domain.customer.service.CustomService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CustomController(
        private val customService: CustomService
) {

    @GetMapping("/user")
    fun findAllFromRedis() = customerSessionRepository.findAll()

    @PostMapping("/user/{userId}")
    fun saveUserSession(@PathVariable("userId") userId: String) {
        customerSessionRepository.save(CustomerSession(userId))


    }
}