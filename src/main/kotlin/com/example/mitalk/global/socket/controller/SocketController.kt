package com.example.mitalk.global.socket.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SocketController {

    @GetMapping("/chat")
    fun getChat(): String {
        println("@ChatController chat Get()")

        return "chat"
    }
}