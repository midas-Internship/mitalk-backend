package com.example.mitalk.global.socket.message.element

import com.example.mitalk.global.annotation.Superclass

@Superclass
abstract class SystemMessage(
    val type: String,
    val message: String
) {
    constructor(messageType: MessageType): this(messageType.name, messageType.message())
}