package com.example.mitalk.global.socket.controller

import com.example.mitalk.global.socket.ChatService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/chat")
@RestController
class SocketController(
    val chatService: ChatService
) {

    @PostMapping
    fun createRoom(@RequestParam name: String) = chatService.createRoom(name)

    @GetMapping
    fun findAllRoom() = chatService.findAllRoom()
}