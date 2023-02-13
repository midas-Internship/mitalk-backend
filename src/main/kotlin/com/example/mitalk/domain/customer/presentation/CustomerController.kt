package com.example.mitalk.domain.customer.presentation

import com.example.mitalk.domain.customer.service.QueueCheckService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class CustomController(
        private val queueCheckService: QueueCheckService
) {

    @GetMapping("/user")
    fun findAllFromRedis() = queueCheckService.execute()

//    @PostMapping("/user/{userId}")
//    fun saveUserSession(@PathVariable("userId") userId: String) {
//        customerSessionRepository.save(CustomerSession(userId))
//
//
//    }
}